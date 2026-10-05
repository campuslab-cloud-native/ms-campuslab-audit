package ms_campuslab_audit.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    private String eventId;
    private Long bookingId;
    private String userId;
    private String type;
    private LocalDateTime timestamp;
    private String source;
}