package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.endpoint.rest.model.UpsertGradeDto;
import school.hei.exam.model.Grade;
import school.hei.exam.model.GradeHistory;
import school.hei.exam.model.Level;

class GradeControllerIT extends AbstractIT {

  @Test
  void grading_creates_the_grade_and_every_change_is_tracked_in_history() {
    var teacher = createTeacher();
    var admin = createAdmin();
    var student = createStudent();
    var course = createCourse();
    var group = createGroup(Level.L2, "2030-2031");
    var offering = createCourseOffering(course, group, "2030-2031", teacher);
    var exam = createExam(offering, "Partiel 1");
    var path = "/exams/" + exam.getId() + "/grades/" + student.getId();

    var firstPut =
        restTemplate.exchange(
            path,
            PUT,
            new HttpEntity<>(
                new UpsertGradeDto(new BigDecimal("15"), "note initiale", teacher.getUserHei().getId())),
            Grade.class);
    assertThat(firstPut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(firstPut.getBody().value()).isEqualByComparingTo("15");
    var gradeId = firstPut.getBody().id();

    var missingReason =
        restTemplate.exchange(
            path,
            PUT,
            new HttpEntity<>(new UpsertGradeDto(new BigDecimal("12"), " ", admin.getId())),
            String.class);
    assertThat(missingReason.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    var missingModifiedBy =
        restTemplate.exchange(
            path,
            PUT,
            new HttpEntity<>(new UpsertGradeDto(new BigDecimal("12"), "erreur", null)),
            String.class);
    assertThat(missingModifiedBy.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    var correction =
        restTemplate.exchange(
            path,
            PUT,
            new HttpEntity<>(
                new UpsertGradeDto(
                    new BigDecimal("12"), "reclamation etudiant, erreur de saisie", admin.getId())),
            Grade.class);
    assertThat(correction.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(correction.getBody().value()).isEqualByComparingTo("12");
    assertThat(correction.getBody().id()).isEqualTo(gradeId);

    var history =
        restTemplate.exchange(
            "/grades/" + gradeId + "/history", GET, HttpEntity.EMPTY, GradeHistory[].class);
    assertThat(history.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(history.getBody()).hasSize(2);

    var latest = history.getBody()[0];
    assertThat(latest.newValue()).isEqualByComparingTo("12");
    assertThat(latest.oldValue()).isEqualByComparingTo("15");
    assertThat(latest.reason()).contains("reclamation");
    assertThat(latest.modifiedById()).isEqualTo(admin.getId());

    var initial = history.getBody()[1];
    assertThat(initial.oldValue()).isNull();
    assertThat(initial.newValue()).isEqualByComparingTo("15");
    assertThat(initial.modifiedById()).isEqualTo(teacher.getUserHei().getId());
  }

  @Test
  void lists_grades_of_an_exam() {
    var teacher = createTeacher();
    var course = createCourse();
    var group = createGroup(Level.L1, "2030-2031");
    var offering = createCourseOffering(course, group, "2030-2031", teacher);
    var exam = createExam(offering, "Partiel 1");
    var student = createStudent();
    createGrade(exam, student, new BigDecimal("11"));

    var response =
        restTemplate.exchange(
            "/exams/" + exam.getId() + "/grades", GET, HttpEntity.EMPTY, Grade[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
  }
}
