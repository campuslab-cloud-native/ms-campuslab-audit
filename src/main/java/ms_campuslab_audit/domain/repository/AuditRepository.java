package ms_campuslab_audit.domain.repository;

import ms_campuslab_audit.domain.model.AuditEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditRepository {

    List<AuditEvent> findByFilters(
            String userId,
            String type,
            LocalDateTime from,
            LocalDateTime to
    );

    List<AuditEvent> findByBookingId(Long bookingId);

    AuditEvent save(AuditEvent event);
}