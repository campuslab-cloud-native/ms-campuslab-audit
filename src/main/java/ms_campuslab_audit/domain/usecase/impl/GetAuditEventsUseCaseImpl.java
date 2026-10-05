package ms_campuslab_audit.domain.usecase.impl;

import lombok.RequiredArgsConstructor;
import ms_campuslab_audit.domain.model.AuditEvent;
import ms_campuslab_audit.domain.repository.AuditRepository;
import ms_campuslab_audit.domain.usecase.GetAuditEventsUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAuditEventsUseCaseImpl
        implements GetAuditEventsUseCase {

    private final AuditRepository repository;

    @Override
    public List<AuditEvent> execute(
            String userId,
            String type,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return repository.findByFilters(
                userId,
                type,
                from,
                to
        );
    }
}