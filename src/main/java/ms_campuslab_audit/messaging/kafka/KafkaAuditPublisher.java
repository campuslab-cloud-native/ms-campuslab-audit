package ms_campuslab_audit.messaging.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ms_campuslab_audit.messaging.model.AuditEvent;
import ms_campuslab_audit.messaging.model.BookingEvent;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class KafkaAuditPublisher {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    @Value("${campuslab.kafka.topics.audit}")
    private String auditTopic;

    public void publish(BookingEvent bookingEvent) {

        AuditEvent auditEvent = new AuditEvent(
                bookingEvent.eventId(),
                bookingEvent.bookingId(),
                bookingEvent.userId(),
                bookingEvent.type(),
                bookingEvent.timestamp(),
                "ms-campuslab-bookings",
                bookingEvent.traceId(),
                bookingEvent.correlationId()
        );

        try {
            kafkaTemplate.send(
                    auditTopic,
                    bookingEvent.bookingId().toString(),
                    auditEvent
            ).get(30, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Publicacion de auditoria interrumpida", e
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error publicando en audit.timeline", e
            );
        }
    }
}

