package ms_campuslab_audit.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDIT_EVENTS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventEntity {

    @Id
    private String eventId;

    private Long bookingId;

    private String userId;

    private String type;

    private LocalDateTime timestamp;

    private String source;
}
