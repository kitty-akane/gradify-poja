package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JGroup;

class GroupMapperTest {

  private final GroupMapper mapper = new GroupMapper();

  @Test
  void maps_entity_to_model() {
    var entity =
        JGroup.builder()
            .id(UUID.randomUUID())
            .ref("GRP-1")
            .level(Level.L1)
            .academicYear("2030-2031")
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.ref()).isEqualTo("GRP-1");
    assertThat(model.level()).isEqualTo(Level.L1);
    assertThat(model.academicYear()).isEqualTo("2030-2031");
  }

  @Test
  void maps_model_to_entity() {
    var model =
        school.hei.exam.model.Group.builder()
            .id(UUID.randomUUID())
            .ref("GRP-1")
            .level(Level.L2)
            .academicYear("2030-2031")
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getLevel()).isEqualTo(Level.L2);
  }

  @Test
  void maps_lists() {
    var e1 = JGroup.builder().id(UUID.randomUUID()).level(Level.L1).build();
    var e2 = JGroup.builder().id(UUID.randomUUID()).level(Level.L3).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);

    var entities = mapper.toEntity(models);
    assertThat(entities).hasSize(2);
  }
}
