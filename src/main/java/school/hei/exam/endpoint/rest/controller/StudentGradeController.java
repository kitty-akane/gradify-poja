package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.exam.authorization.AuthorizationService;
import school.hei.exam.endpoint.event.EventProducer;
import school.hei.exam.endpoint.event.model.SendTranscriptRequested;
import school.hei.exam.exception.ForbiddenException;
import school.hei.exam.model.Grade;
import school.hei.exam.service.GradeService;

@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
public class StudentGradeController {

  private final AuthorizationService authorizationService;
  private final GradeService gradeService;
  private final EventProducer<SendTranscriptRequested> eventProducer;

  @GetMapping("/{studentId}/grades")
  public List<Grade> getMyGrades(@PathVariable UUID studentId, @RequestParam String academicYear) {
    if (!authorizationService.isSelfStudentOrAdmin(studentId)) {
      throw new ForbiddenException("Vous ne pouvez consulter que vos propres notes");
    }

    return gradeService.getGradesForStudent(studentId, academicYear).stream()
        .map(this::toModel)
        .toList();
  }

  @PostMapping("/{studentId}/transcript/send")
  public ResponseEntity<Void> sendTranscriptByEmail(@PathVariable UUID studentId) {
    if (!authorizationService.isSelfStudentOrAdmin(studentId)) {
      throw new ForbiddenException("Vous ne pouvez recevoir que votre propre relevé");
    }

    eventProducer.accept(List.of(SendTranscriptRequested.builder().studentId(studentId).build()));

    return ResponseEntity.accepted().build();
  }

  private Grade toModel(Grade grade) {
    return new Grade(grade.id(), grade.examId(), grade.studentId(), grade.value());
  }
}
