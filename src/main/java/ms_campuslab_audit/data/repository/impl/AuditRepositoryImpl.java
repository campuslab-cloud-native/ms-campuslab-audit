package ms_campuslab_audit.data.repository.impl;

import lombok.RequiredArgsConstructor;
import ms_campuslab_audit.data.mapper.AuditDataMapper;
import ms_campuslab_audit.data.model.AuditEventEntity;
import ms_campuslab_audit.data.repository.AuditJpaRepository;
import ms_campuslab_audit.domain.model.AuditEvent;
import ms_campuslab_audit.domain.repository.AuditRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {
    private final AuditJpaRepository auditJpaRepository;
    private final AuditDataMapper auditDataMapper;

    @Override
    public List<AuditEvent> findByFilters(
            String userId,
            String type,
            LocalDateTime from,
            LocalDateTime to
    ) {

        Specification<AuditEventEntity> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();

        if (userId != null && !userId.isBlank()) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("userId"), userId)
            );
        }

        if (type != null && !type.isBlank()) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("type"), type)
            );
        }

        if (from != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.greaterThanOrEqualTo(
                                    root.get("timestamp"),
                                    from
                            )
            );
        }

        if (to != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.lessThanOrEqualTo(
                                    root.get("timestamp"),
                                    to
                            )
            );
        }

        return auditJpaRepository.findAll(specification)
                .stream()
                .map(auditDataMapper::toDomain)
                .toList();
    }

    @Override
    public List<AuditEvent> findByBookingId(Long bookingId) {
        return auditJpaRepository
                .findByBookingIdOrderByTimestampAsc(bookingId)
                .stream()
                .map(auditDataMapper::toDomain)
                .toList();
    }

    @Override
    public AuditEvent save(AuditEvent event) {
        return auditDataMapper.toDomain(
                auditJpaRepository.save(
                        auditDataMapper.toEntity(event)
                )
        );
    }
}
