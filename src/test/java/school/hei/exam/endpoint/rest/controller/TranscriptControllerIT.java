package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.endpoint.rest.model.TranscriptRequestAckDto;

class TranscriptControllerIT extends AbstractIT {

  @Test
void accepts_a_transcript_request() {
  var student = createStudent();

  var response =
      restTemplate.exchange(
          "/students/" + student.getId() + "/transcript:send",
          POST,
          HttpEntity.EMPTY,
          TranscriptRequestAckDto.class);

  System.out.println("STATUS: " + response.getStatusCode());
  System.out.println("BODY: " + response.getBody());

  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
  assertThat(response.getBody().status()).isEqualTo("PENDING");
  assertThat(response.getBody().studentId()).isEqualTo(student.getId());
}
}
