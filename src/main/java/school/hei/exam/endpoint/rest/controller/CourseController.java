package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.CourseMapper;
import school.hei.exam.model.Course;
import school.hei.exam.repository.CourseRepository;
import school.hei.exam.repository.model.JCourse;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;

  @GetMapping
  public List<Course> getCourses() {
    return courseMapper.toModel(courseRepository.findAll());
  }

  @PostMapping
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    var toSave =
        JCourse.builder().ref(course.ref()).title(course.title()).credits(course.credits()).build();
    var saved = courseRepository.save(toSave);
    return ResponseEntity.status(HttpStatus.CREATED).body(courseMapper.toModel(saved));
  }
}
