package ms_campuslab_audit.domain.usecase;

import ms_campuslab_audit.domain.model.AuditEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface GetAuditEventsUseCase {

    List<AuditEvent> execute(
            String userId,
            String type,
            LocalDateTime from,
            LocalDateTime to
    );
}
