package ms_campuslab_audit.data.mapper;

import ms_campuslab_audit.data.model.AuditEventEntity;
import ms_campuslab_audit.domain.model.AuditEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditDataMapper {

    public AuditEventEntity toEntity(AuditEvent event) {
        return AuditEventEntity.builder()
                .eventId(event.getEventId())
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .type(event.getType())
                .timestamp(event.getTimestamp())
                .source(event.getSource())
                .build();
    }

    public AuditEvent toDomain(AuditEventEntity entity) {
        return AuditEvent.builder()
                .eventId(entity.getEventId())
                .bookingId(entity.getBookingId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .timestamp(entity.getTimestamp())
                .source(entity.getSource())
                .build();
    }
}