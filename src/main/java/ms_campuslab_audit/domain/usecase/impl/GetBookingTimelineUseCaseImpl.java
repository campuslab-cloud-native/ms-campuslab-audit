package ms_campuslab_audit.domain.usecase.impl;

import lombok.RequiredArgsConstructor;
import ms_campuslab_audit.domain.model.AuditEvent;
import ms_campuslab_audit.domain.repository.AuditRepository;
import ms_campuslab_audit.domain.usecase.GetBookingTimelineUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBookingTimelineUseCaseImpl
        implements GetBookingTimelineUseCase {

    private final AuditRepository repository;

    @Override
    public List<AuditEvent> execute(Long bookingId) {
        return repository.findByBookingId(bookingId);
    }
}