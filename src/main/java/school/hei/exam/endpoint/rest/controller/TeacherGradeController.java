package school.hei.exam.endpoint.rest.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.hei.exam.authorization.AuthorizationService;
import school.hei.exam.exception.ForbiddenException;
import school.hei.exam.model.Grade;
import school.hei.exam.model.UpdateGradeRequest;
import school.hei.exam.security.SecurityUtils;
import school.hei.exam.service.GradeService;

@RestController
@RequestMapping("/api/teacher")
@AllArgsConstructor
public class TeacherGradeController {

  private final AuthorizationService authorizationService;
  private final GradeService gradeService;

  @GetMapping("/course-offerings/{courseOfferingId}/grades")
  public List<Grade> getGradesForMyCourse(@PathVariable UUID courseOfferingId) {
    if (!authorizationService.teachesCourseOfferingOrAdmin(courseOfferingId)) {
      throw new ForbiddenException("Vous n'enseignez pas ce cours");
    }

    return gradeService.getGradesForCourseOffering(courseOfferingId).stream()
        .map(this::toModel)
        .toList();
  }

  @PutMapping("/grades/{gradeId}")
  public Grade updateGrade(
      @PathVariable UUID gradeId, @Valid @RequestBody UpdateGradeRequest request) {
    if (!authorizationService.canModifyGradeOrAdmin(gradeId)) {
      throw new ForbiddenException("Vous ne pouvez modifier que les notes de vos cours");
    }

    Grade updated =
        gradeService.updateGrade(
            gradeId, request.newValue(), request.reason(), SecurityUtils.currentUserId());

    return toModel(updated);
  }

  private Grade toModel(Grade grade) {
    return new Grade(grade.id(), grade.examId(), grade.studentId(), grade.value());
  }
}
