package school.hei.exam.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Course;
import school.hei.exam.repository.model.JCourse;

@Component
public class CourseMapper {
  public List<Course> toModel(List<JCourse> courses) {
    return courses.stream().map(this::toModel).toList();
  }

  public Course toModel(JCourse course) {
    return Course.builder()
        .id(course.getId())
        .ref(course.getRef())
        .title(course.getTitle())
        .credits(course.getCredits())
        .build();
  }

  public List<JCourse> toEntity(List<Course> courses) {
    return courses.stream().map(this::toEntity).toList();
  }

  public JCourse toEntity(Course course) {
    return JCourse.builder()
        .id(course.id())
        .ref(course.ref())
        .title(course.title())
        .credits(course.credits())
        .build();
  }
}
