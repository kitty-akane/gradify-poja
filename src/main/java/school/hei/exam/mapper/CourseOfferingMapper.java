package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.CourseOffering;
import school.hei.exam.repository.model.JCourse;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JTeacher;

@Component
@AllArgsConstructor
public class CourseOfferingMapper {
  private final EntityManager entityManager;

  public List<CourseOffering> toModel(List<JCourseOffering> courseOfferings) {
    return courseOfferings.stream().map(this::toModel).toList();
  }

  public CourseOffering toModel(JCourseOffering courseOffering) {
    return CourseOffering.builder()
        .id(courseOffering.getId())
        .courseId(courseOffering.getCourse().getId())
        .groupId(courseOffering.getGroup().getId())
        .academicYear(courseOffering.getAcademicYear())
        .teacherIds(courseOffering.getTeachers().stream().map(JTeacher::getId).toList())
        .build();
  }

  public List<JCourseOffering> toEntity(List<CourseOffering> courseOfferings) {
    return courseOfferings.stream().map(this::toEntity).toList();
  }

  public JCourseOffering toEntity(CourseOffering courseOffering) {
    JCourse course = entityManager.getReference(JCourse.class, courseOffering.courseId());
    JGroup group = entityManager.getReference(JGroup.class, courseOffering.groupId());
    List<JTeacher> teachers =
        courseOffering.teacherIds().stream()
            .map(id -> entityManager.getReference(JTeacher.class, id))
            .toList();
    return JCourseOffering.builder()
        .id(courseOffering.id())
        .course(course)
        .group(group)
        .academicYear(courseOffering.academicYear())
        .teachers(teachers)
        .build();
  }
}
