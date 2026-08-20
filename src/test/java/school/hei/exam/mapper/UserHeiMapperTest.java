package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.model.Role;
import school.hei.exam.repository.model.JUserHei;

class UserHeiMapperTest {

  private final UserHeiMapper mapper = new UserHeiMapper();

  @Test
  void maps_entity_to_model() {
    var entity =
        JUserHei.builder()
            .id(UUID.randomUUID())
            .firstName("Ana")
            .lastName("Rakoto")
            .email("ana@hei.school")
            .phoneNumber("+261340000000")
            .address("Antananarivo")
            .role(Role.ADMIN)
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.firstName()).isEqualTo("Ana");
    assertThat(model.lastName()).isEqualTo("Rakoto");
    assertThat(model.email()).isEqualTo("ana@hei.school");
    assertThat(model.phoneNumber()).isEqualTo("+261340000000");
    assertThat(model.address()).isEqualTo("Antananarivo");
    assertThat(model.role()).isEqualTo(Role.ADMIN);
  }

  @Test
  void maps_model_to_entity() {
    var model =
        school.hei.exam.model.UserHei.builder()
            .id(UUID.randomUUID())
            .firstName("Ana")
            .lastName("Rakoto")
            .email("ana@hei.school")
            .phoneNumber("+261340000000")
            .address("Antananarivo")
            .role(Role.ADMIN)
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getFirstName()).isEqualTo("Ana");
    assertThat(entity.getEmail()).isEqualTo("ana@hei.school");
    assertThat(entity.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  void maps_lists() {
    var e1 = JUserHei.builder().id(UUID.randomUUID()).role(Role.STUDENT).build();
    var e2 = JUserHei.builder().id(UUID.randomUUID()).role(Role.TEACHER).build();

    var models = mapper.toModel(List.of(e1, e2));

    assertThat(models).hasSize(2);
    assertThat(models.get(0).role()).isEqualTo(Role.STUDENT);
    assertThat(models.get(1).role()).isEqualTo(Role.TEACHER);
  }
}
