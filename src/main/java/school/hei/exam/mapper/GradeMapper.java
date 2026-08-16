package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Grade;
import school.hei.exam.repository.model.JExam;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JStudent;

@Component
@AllArgsConstructor
public class GradeMapper {
  private final EntityManager entityManager;

  public List<Grade> toModel(List<JGrade> grades) {
    return grades.stream().map(this::toModel).toList();
  }

  public Grade toModel(JGrade grade) {
    return Grade.builder()
        .id(grade.getId())
        .examId(grade.getExam().getId())
        .studentId(grade.getStudent().getId())
        .value(grade.getValue())
        .build();
  }

  public List<JGrade> toEntity(List<Grade> grades) {
    return grades.stream().map(this::toEntity).toList();
  }

  public JGrade toEntity(Grade grade) {
    JExam exam = entityManager.getReference(JExam.class, grade.examId());
    JStudent student = entityManager.getReference(JStudent.class, grade.studentId());
    return JGrade.builder().id(grade.id()).exam(exam).student(student).value(grade.value()).build();
  }
}
