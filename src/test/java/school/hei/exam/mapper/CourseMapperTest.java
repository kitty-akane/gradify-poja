package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.repository.model.JCourse;

class CourseMapperTest {

  private final CourseMapper mapper = new CourseMapper();

  @Test
  void maps_entity_to_model() {
    var entity =
        JCourse.builder().id(UUID.randomUUID()).ref("ALG101").title("Algo").credits(6).build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.ref()).isEqualTo("ALG101");
    assertThat(model.title()).isEqualTo("Algo");
    assertThat(model.credits()).isEqualTo(6);
  }

  @Test
  void maps_model_to_entity() {
    var model =
        school.hei.exam.model.Course.builder()
            .id(UUID.randomUUID())
            .ref("ALG101")
            .title("Algo")
            .credits(6)
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getRef()).isEqualTo("ALG101");
    assertThat(entity.getCredits()).isEqualTo(6);
  }

  @Test
  void maps_lists() {
    var e1 = JCourse.builder().id(UUID.randomUUID()).ref("A").credits(1).build();
    var e2 = JCourse.builder().id(UUID.randomUUID()).ref("B").credits(2).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);

    var entities = mapper.toEntity(models);
    assertThat(entities).hasSize(2);
  }
}
