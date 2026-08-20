package school.hei.exam.endpoint.rest.controller;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import school.hei.exam.endpoint.rest.model.UpsertGradeDto;
import school.hei.exam.mapper.GradeHistoryMapper;
import school.hei.exam.mapper.GradeMapper;
import school.hei.exam.model.Grade;
import school.hei.exam.model.GradeHistory;
import school.hei.exam.repository.GradeHistoryRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.model.JExam;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JGradeHistory;
import school.hei.exam.repository.model.JStudent;
import school.hei.exam.repository.model.JUserHei;

@RestController
@AllArgsConstructor
public class GradeController {

  private final GradeRepository gradeRepository;
  private final GradeHistoryRepository gradeHistoryRepository;
  private final GradeMapper gradeMapper;
  private final GradeHistoryMapper gradeHistoryMapper;
  private final EntityManager entityManager;

  @GetMapping("/exams/{examId}/grades")
  public List<Grade> getGradesByExam(@PathVariable UUID examId) {
    return gradeMapper.toModel(gradeRepository.findByExam_Id(examId));
  }

  @PutMapping("/exams/{examId}/grades/{studentId}")
  public Grade upsertGrade(
      @PathVariable UUID examId, @PathVariable UUID studentId, @RequestBody UpsertGradeDto dto) {
    if (dto.reason() == null || dto.reason().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reason is required to set or change a grade");
    }
    if (dto.modifiedById() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "modifiedById is required");
    }

    var existing = gradeRepository.findByExam_IdAndStudent_Id(examId, studentId);
    var oldValue = existing.map(JGrade::getValue).orElse(null);

    JGrade grade;
    if (existing.isPresent()) {
      grade = existing.get();
      grade.setValue(dto.value());
      grade = gradeRepository.save(grade);
    } else {
      var exam = entityManager.getReference(JExam.class, examId);
      var student = entityManager.getReference(JStudent.class, studentId);
      grade =
          gradeRepository.save(
              JGrade.builder().exam(exam).student(student).value(dto.value()).build());
    }

    var modifiedBy = entityManager.getReference(JUserHei.class, dto.modifiedById());
    gradeHistoryRepository.save(
        JGradeHistory.builder()
            .grade(grade)
            .oldValue(oldValue)
            .newValue(dto.value())
            .reason(dto.reason())
            .modifiedById(modifiedBy)
            .modifiedAt(Instant.now())
            .build());

    return gradeMapper.toModel(grade);
  }

  @GetMapping("/grades/{gradeId}/history")
  public List<GradeHistory> getGradeHistory(@PathVariable UUID gradeId) {
    return gradeHistoryMapper.toModel(
        gradeHistoryRepository.findByGrade_Id(gradeId, Sort.by(Sort.Direction.DESC, "modifiedAt")));
  }
}
