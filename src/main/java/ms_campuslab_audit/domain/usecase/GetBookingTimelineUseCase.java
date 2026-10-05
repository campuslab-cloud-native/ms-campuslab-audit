package ms_campuslab_audit.domain.usecase;



import ms_campuslab_audit.domain.model.AuditEvent;

import java.util.List;

public interface GetBookingTimelineUseCase {

    List<AuditEvent> execute(Long bookingId);
}
