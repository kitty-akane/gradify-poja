package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.Course;

class CourseControllerIT extends AbstractIT {

  @Test
  void creates_a_course() {
    var body = Course.builder().ref("ALG101").title("Algorithmique").credits(6).build();

    var response = restTemplate.exchange("/courses", POST, new HttpEntity<>(body), Course.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().ref()).isEqualTo("ALG101");
    assertThat(response.getBody().title()).isEqualTo("Algorithmique");
    assertThat(response.getBody().credits()).isEqualTo(6);
    assertThat(courseRepository.findById(response.getBody().id())).isPresent();
  }

  @Test
  void lists_courses() {
    createCourse();
    createCourse();

    var response = restTemplate.exchange("/courses", GET, HttpEntity.EMPTY, Course[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(2);
  }
}
