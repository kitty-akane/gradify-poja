package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Exam;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JExam;

@Component
@AllArgsConstructor
public class ExamMapper {
  private final EntityManager entityManager;

  public List<Exam> toModel(List<JExam> exams) {
    return exams.stream().map(this::toModel).toList();
  }

  public Exam toModel(JExam exam) {
    return Exam.builder()
        .id(exam.getId())
        .offeringId(exam.getCourseOffering().getId())
        .label(exam.getLabel())
        .examDate(exam.getExamDate())
        .coefficient(exam.getCoefficient())
        .build();
  }

  public List<JExam> toEntity(List<Exam> exams) {
    return exams.stream().map(this::toEntity).toList();
  }

  public JExam toEntity(Exam exam) {
    JCourseOffering courseOffering =
        entityManager.getReference(JCourseOffering.class, exam.offeringId());
    return JExam.builder()
        .id(exam.id())
        .courseOffering(courseOffering)
        .label(exam.label())
        .examDate(exam.examDate())
        .coefficient(exam.coefficient())
        .build();
  }
}
