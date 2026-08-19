package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.endpoint.event.EventProducer;
import school.hei.exam.endpoint.event.model.TranscriptRequested;
import school.hei.exam.endpoint.rest.model.TranscriptRequestAckDto;

@RestController
@AllArgsConstructor
public class TranscriptController {

  private final EventProducer<TranscriptRequested> eventProducer;

  @PostMapping("/students/{studentId}/transcript:send")
  public ResponseEntity<TranscriptRequestAckDto> sendStudentTranscript(
      @PathVariable UUID studentId) {
    eventProducer.accept(
        List.of(TranscriptRequested.builder().studentId(studentId.toString()).build()));

    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(TranscriptRequestAckDto.pending(studentId));
  }
}
