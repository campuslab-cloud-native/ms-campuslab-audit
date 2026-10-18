package ms_campuslab_audit.presentation.controller;

import lombok.RequiredArgsConstructor;
import ms_campuslab_audit.domain.model.AuditEvent;
import ms_campuslab_audit.domain.usecase.GetAuditEventsUseCase;
import ms_campuslab_audit.domain.usecase.GetBookingTimelineUseCase;
import ms_campuslab_audit.presentation.response.AuditEventResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final GetAuditEventsUseCase getAuditEventsUseCase;
    private final GetBookingTimelineUseCase getBookingTimelineUseCase;

    @GetMapping("/events")
    public ResponseEntity<List<AuditEventResponse>> getAuditEvents(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {

        return ResponseEntity.ok(
                getAuditEventsUseCase
                        .execute(userId, type, from, to)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<List<AuditEventResponse>> getBookingTimeline(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                getBookingTimelineUseCase
                        .execute(bookingId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private AuditEventResponse toResponse(AuditEvent event) {
        return AuditEventResponse.builder()
                .eventId(event.getEventId())
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .type(event.getType())
                .timestamp(event.getTimestamp())
                .source(event.getSource())
                .build();
    }
}
