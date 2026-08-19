package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.CourseOfferingMapper;
import school.hei.exam.mapper.TeacherMapper;
import school.hei.exam.model.CourseOffering;
import school.hei.exam.model.Teacher;
import school.hei.exam.repository.TeacherRepository;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {

  private final TeacherRepository teacherRepository;
  private final TeacherMapper teacherMapper;
  private final CourseOfferingMapper courseOfferingMapper;

  @GetMapping
  public List<Teacher> getTeachers() {
    return teacherMapper.toModel(teacherRepository.findAll());
  }

  @GetMapping("/{teacherId}/course-offerings")
  public List<CourseOffering> getTeacherCourseOfferings(@PathVariable UUID teacherId) {
    var teacher = teacherRepository.findById(teacherId).orElseThrow();
    return courseOfferingMapper.toModel(teacher.getCourseOfferings());
  }
}
