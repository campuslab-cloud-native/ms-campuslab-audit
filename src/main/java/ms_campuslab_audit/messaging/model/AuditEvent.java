package ms_campuslab_audit.messaging.model;

import java.time.OffsetDateTime;

public record AuditEvent(
        String eventId,
        Long bookingId,
        String userId,
        String type,
        OffsetDateTime timestamp,
        String source,
        String traceId,
        String correlationId
) {
}

