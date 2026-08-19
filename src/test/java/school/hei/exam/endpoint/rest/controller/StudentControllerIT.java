package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.Enrollment;
import school.hei.exam.model.Grade;
import school.hei.exam.model.Level;
import school.hei.exam.model.Student;
import school.hei.exam.model.Track;

class StudentControllerIT extends AbstractIT {

  @Test
  void gets_a_student_by_id() {
    var student = createStudent();

    var response =
        restTemplate.exchange(
            "/students/" + student.getId(), GET, HttpEntity.EMPTY, Student.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().id()).isEqualTo(student.getId());
    assertThat(response.getBody().studentNumber()).isEqualTo(student.getStudentNumber());
  }

  @Test
  void lists_students_filtered_by_group() {
    var group = createGroup(Level.L1, "2030-2031");
    var inGroup = createStudent();
    enroll(inGroup, group, "2030-2031", Level.L1);
    createStudent(); // not enrolled in that group

    var response =
        restTemplate.exchange(
            "/students?groupId=" + group.getId(), GET, HttpEntity.EMPTY, Student[].class);

    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()[0].id()).isEqualTo(inGroup.getId());
  }

  @Test
  void gets_a_students_grades() {
    var teacher = createTeacher();
    var student = createStudent();
    var course = createCourse();
    var group = createGroup(Level.L1, "2030-2031");
    var offering = createCourseOffering(course, group, "2030-2031", teacher);
    var exam = createExam(offering, "Partiel 1");
    createGrade(exam, student, new BigDecimal("14"));

    var response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/grades", GET, HttpEntity.EMPTY, Grade[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()[0].value()).isEqualByComparingTo("14");
  }

  @Test
  void changes_a_students_group_and_keeps_enrollment_history() {
    var student = createStudent();
    var oldGroup = createGroup(Level.L1, "2030-2031");
    var newGroup = createGroup(Level.L1, "2030-2031");
    enroll(student, oldGroup, "2030-2031", Level.L1);

    var body =
        Enrollment.builder()
            .groupId(newGroup.getId())
            .academicYear("2030-2031")
            .level(Level.L1)
            .track(Track.TRONC_COMMUN)
            .build();

    var response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/enrollments",
            POST,
            new HttpEntity<>(body),
            Enrollment.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().groupId()).isEqualTo(newGroup.getId());

    var enrollments =
        restTemplate.exchange(
            "/students/" + student.getId() + "/enrollments",
            GET,
            HttpEntity.EMPTY,
            Enrollment[].class);
    assertThat(enrollments.getBody()).hasSize(2);
  }
}
