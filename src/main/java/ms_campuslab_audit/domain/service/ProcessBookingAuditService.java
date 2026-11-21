package ms_campuslab_audit.domain.service;

import lombok.RequiredArgsConstructor;
import ms_campuslab_audit.domain.model.AuditEvent;
import ms_campuslab_audit.domain.repository.AuditRepository;
import ms_campuslab_audit.messaging.kafka.KafkaAuditPublisher;
import ms_campuslab_audit.messaging.model.BookingEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class ProcessBookingAuditService {

    private final AuditRepository auditRepository;
    private final KafkaAuditPublisher kafkaAuditPublisher;

    public void process(BookingEvent bookingEvent) {

        if (bookingEvent == null
                || bookingEvent.eventId() == null
                || bookingEvent.bookingId() == null
                || bookingEvent.type() == null
                || bookingEvent.timestamp() == null) {
            throw new IllegalArgumentException(
                    "Evento de reserva invalido"
            );
        }

        AuditEvent auditEvent = AuditEvent.builder()
                .eventId(bookingEvent.eventId())
                .bookingId(bookingEvent.bookingId())
                .userId(bookingEvent.userId())
                .type(bookingEvent.type())
                .timestamp(
                        LocalDateTime.ofInstant(
                                bookingEvent.timestamp().toInstant(),
                                ZoneOffset.UTC
                        )
                )
                .source("ms-campuslab-bookings")
                .build();

        auditRepository.save(auditEvent);

        kafkaAuditPublisher.publish(bookingEvent);
    }
}

