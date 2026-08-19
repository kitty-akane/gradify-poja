package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.model.Group;
import school.hei.exam.model.Level;

class GroupControllerIT extends AbstractIT {

  @Test
  void filters_groups_by_academic_year_and_level() {
    var year = "2030-2031";
    var otherYear = "2029-2030";
    createGroup(Level.L1, year);
    createGroup(Level.L3, year);
    createGroup(Level.L1, otherYear);

    var response =
        restTemplate.exchange(
            "/groups?academicYear=" + year + "&level=L1", GET, HttpEntity.EMPTY, Group[].class);

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody())
        .allMatch(g -> g.academicYear().equals(year) && g.level() == Level.L1);
    assertThat(response.getBody()).hasSize(1);
  }

  @Test
  void creates_a_group() {
    var body = Group.builder().ref("L1-A").level(Level.L1).academicYear("2031-2032").build();

    var response =
        restTemplate.exchange(
            "/groups", org.springframework.http.HttpMethod.POST, new HttpEntity<>(body), Group.class);

    assertThat(response.getStatusCode())
        .isEqualTo(org.springframework.http.HttpStatus.CREATED);
    assertThat(response.getBody().ref()).isEqualTo("L1-A");
  }
}
