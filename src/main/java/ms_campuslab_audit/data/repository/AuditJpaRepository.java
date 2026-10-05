package ms_campuslab_audit.data.repository;

import ms_campuslab_audit.data.model.AuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuditJpaRepository
        extends JpaRepository<AuditEventEntity, String>,
        JpaSpecificationExecutor<AuditEventEntity> {

    List<AuditEventEntity> findByBookingIdOrderByTimestampAsc(Long bookingId);
}