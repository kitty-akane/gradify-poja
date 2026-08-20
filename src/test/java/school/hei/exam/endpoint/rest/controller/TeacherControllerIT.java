package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.CourseOffering;
import school.hei.exam.model.Level;
import school.hei.exam.model.Teacher;

class TeacherControllerIT extends AbstractIT {

  @Test
  void lists_teachers() {
    createTeacher();

    var response = restTemplate.exchange("/teachers", GET, HttpEntity.EMPTY, Teacher[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void lists_a_teachers_course_offerings() {
    var teacher = createTeacher();
    var course = createCourse();
    var group = createGroup(Level.L2, "2030-2031");
    createCourseOffering(course, group, "2030-2031", teacher);

    var response =
        restTemplate.exchange(
            "/teachers/" + teacher.getId() + "/course-offerings",
            GET,
            HttpEntity.EMPTY,
            CourseOffering[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
  }
}
