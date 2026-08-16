package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Student;
import school.hei.exam.repository.model.JStudent;
import school.hei.exam.repository.model.JUserHei;

@Component
@AllArgsConstructor
public class StudentMapper {
  private final EntityManager entityManager;

  public List<Student> toModel(List<JStudent> students) {
    return students.stream().map(this::toModel).toList();
  }

  public Student toModel(JStudent student) {
    return Student.builder()
        .id(student.getId())
        .userId(student.getUserHei().getId())
        .studentNumber(student.getStudentNumber())
        .entryDate(student.getEntryDate())
        .build();
  }

  public List<JStudent> toEntity(List<Student> students) {
    return students.stream().map(this::toEntity).toList();
  }

  public JStudent toEntity(Student student) {
    JUserHei userHei = entityManager.getReference(JUserHei.class, student.userId());
    return JStudent.builder()
        .id(student.id())
        .userHei(userHei)
        .studentNumber(student.studentNumber())
        .entryDate(student.entryDate())
        .build();
  }
}
