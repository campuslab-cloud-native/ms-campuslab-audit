package ms_campuslab_audit.messaging.kafka;

import ms_campuslab_audit.messaging.model.AuditEvent;
import ms_campuslab_audit.messaging.model.BookingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentCaptor.forClass;

class KafkaAuditPublisherTest {

    @Test
    void publishPreservesBookingEventId() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, AuditEvent> kafkaTemplate = mock(KafkaTemplate.class);
        when(kafkaTemplate.send(
                eq("audit.timeline"),
                eq("booking-event-123"),
                any(AuditEvent.class)
        )).thenReturn(CompletableFuture.completedFuture(null));

        KafkaAuditPublisher publisher = new KafkaAuditPublisher(kafkaTemplate);
        ReflectionTestUtils.setField(publisher, "auditTopic", "audit.timeline");
        BookingEvent bookingEvent = new BookingEvent(
                "booking-event-123",
                "BOOKING_CREATED",
                42L,
                7L,
                "user-1",
                OffsetDateTime.parse("2026-11-21T17:00:00Z"),
                "trace-1",
                "correlation-1"
        );

        publisher.publish(bookingEvent);

        var publishedEvent = forClass(AuditEvent.class);
        verify(kafkaTemplate).send(
                eq("audit.timeline"),
                eq("booking-event-123"),
                publishedEvent.capture()
        );
        assertEquals(bookingEvent.eventId(), publishedEvent.getValue().eventId());
    }
}
