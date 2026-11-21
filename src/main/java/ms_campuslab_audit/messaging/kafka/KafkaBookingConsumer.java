package ms_campuslab_audit.messaging.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ms_campuslab_audit.domain.service.ProcessBookingAuditService;
import ms_campuslab_audit.messaging.model.BookingEvent;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBookingConsumer {

    private final ProcessBookingAuditService processBookingAuditService;

    @KafkaListener(
            topics = "${campuslab.kafka.topics.bookings}"
    )
    public void consume(BookingEvent event) {

        log.info(
                "Evento Kafka recibido. eventId={}, bookingId={}, type={}",
                event.eventId(),
                event.bookingId(),
                event.type()
        );

        processBookingAuditService.process(event);

        log.info(
                "Evento Kafka procesado. eventId={}",
                event.eventId()
        );
    }
}
