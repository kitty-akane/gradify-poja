package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.Exam;
import school.hei.exam.model.Level;

class ExamControllerIT extends AbstractIT {

  @Test
  void creates_and_lists_exams_for_an_offering() {
    var course = createCourse();
    var group = createGroup(Level.L2, "2030-2031");
    var teacher = createTeacher();
    var offering = createCourseOffering(course, group, "2030-2031", teacher);

    var body =
        Exam.builder()
            .offeringId(offering.getId())
            .label("Partiel 1")
            .examDate(Instant.now())
            .coefficient(new BigDecimal("2"))
            .build();
    var created =
        restTemplate.exchange(
            "/course-offerings/" + offering.getId() + "/exams", POST, new HttpEntity<>(body), Exam.class);

    assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(created.getBody().label()).isEqualTo("Partiel 1");
    assertThat(created.getBody().offeringId()).isEqualTo(offering.getId());

    var listed =
        restTemplate.exchange(
            "/course-offerings/" + offering.getId() + "/exams", GET, HttpEntity.EMPTY, Exam[].class);
    assertThat(listed.getBody()).hasSize(1);
  }

  @Test
  void rejects_mismatched_offering_id_between_path_and_body() {
    var course = createCourse();
    var groupA = createGroup(Level.L2, "2030-2031");
    var groupB = createGroup(Level.L1, "2030-2031");
    var teacher = createTeacher();
    var offering = createCourseOffering(course, groupA, "2030-2031", teacher);
    var anotherOffering = createCourseOffering(course, groupB, "2030-2031", teacher);

    var body =
        Exam.builder()
            .offeringId(anotherOffering.getId())
            .label("Partiel 1")
            .examDate(Instant.now())
            .coefficient(BigDecimal.ONE)
            .build();

    var response =
        restTemplate.exchange(
            "/course-offerings/" + offering.getId() + "/exams",
            POST,
            new HttpEntity<>(body),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
