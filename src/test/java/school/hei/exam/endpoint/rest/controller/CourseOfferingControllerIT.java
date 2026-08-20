package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.CourseOffering;
import school.hei.exam.model.Level;

class CourseOfferingControllerIT extends AbstractIT {

  @Test
  void creates_a_course_offering_with_several_teachers() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2030-2031");
    var teacherA = createTeacher();
    var teacherB = createTeacher();

    var body =
        CourseOffering.builder()
            .courseId(course.getId())
            .groupId(group.getId())
            .academicYear("2030-2031")
            .teacherIds(List.of(teacherA.getId(), teacherB.getId()))
            .build();

    var response =
        restTemplate.exchange(
            "/course-offerings", POST, new HttpEntity<>(body), CourseOffering.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().teacherIds())
        .containsExactlyInAnyOrder(teacherA.getId(), teacherB.getId());
  }

  @Test
  void filters_course_offerings_by_group_and_academic_year() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2030-2031");
    var teacher = createTeacher();
    var offering = createCourseOffering(course, group, "2030-2031", teacher);
    createCourseOffering(course, createGroup(Level.L1, "2030-2031"), "2030-2031", teacher);

    var response =
        restTemplate.exchange(
            "/course-offerings?groupId=" + group.getId() + "&academicYear=2030-2031",
            GET,
            HttpEntity.EMPTY,
            CourseOffering[].class);

    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()[0].id()).isEqualTo(offering.getId());
  }

  @Test
  void filters_course_offerings_by_group_only() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2031-2032");
    var teacher = createTeacher();
    createCourseOffering(course, group, "2031-2032", teacher);

    var response =
        restTemplate.exchange(
            "/course-offerings?groupId=" + group.getId(),
            GET,
            HttpEntity.EMPTY,
            CourseOffering[].class);

    assertThat(response.getBody()).hasSize(1);
  }

  @Test
  void filters_course_offerings_by_academic_year_only() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2032-2033");
    var teacher = createTeacher();
    createCourseOffering(course, group, "2032-2033", teacher);

    var response =
        restTemplate.exchange(
            "/course-offerings?academicYear=2032-2033",
            GET,
            HttpEntity.EMPTY,
            CourseOffering[].class);

    assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  void filters_course_offerings_by_teacher_only() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2033-2034");
    var teacher = createTeacher();
    createCourseOffering(course, group, "2033-2034", teacher);

    var response =
        restTemplate.exchange(
            "/course-offerings?teacherId=" + teacher.getId(),
            GET,
            HttpEntity.EMPTY,
            CourseOffering[].class);

    assertThat(response.getBody()).hasSize(1);
  }

  @Test
  void lists_all_course_offerings_without_filters() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2034-2035");
    var teacher = createTeacher();
    createCourseOffering(course, group, "2034-2035", teacher);

    var response =
        restTemplate.exchange(
            "/course-offerings", GET, HttpEntity.EMPTY, CourseOffering[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
  }
}
