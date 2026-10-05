package ms_campuslab_audit.presentation.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventResponse {

    private String eventId;
    private Long bookingId;
    private String userId;
    private String type;
    private LocalDateTime timestamp;
    private String source;
}