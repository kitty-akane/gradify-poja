package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Teacher;
import school.hei.exam.repository.model.JTeacher;
import school.hei.exam.repository.model.JUserHei;

@Component
@AllArgsConstructor
public class TeacherMapper {
  private final EntityManager entityManager;

  public List<Teacher> toModel(List<JTeacher> teachers) {
    return teachers.stream().map(this::toModel).toList();
  }

  public Teacher toModel(JTeacher teacher) {
    return Teacher.builder().id(teacher.getId()).userId(teacher.getUserHei().getId()).build();
  }

  public List<JTeacher> toEntity(List<Teacher> teachers) {
    return teachers.stream().map(this::toEntity).toList();
  }

  public JTeacher toEntity(Teacher teacher) {
    JUserHei userHei = entityManager.getReference(JUserHei.class, teacher.userId());
    return JTeacher.builder().id(teacher.id()).userHei(userHei).build();
  }
}
