package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.repository.model.JStudent;
import school.hei.exam.repository.model.JUserHei;

class StudentMapperTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final StudentMapper mapper = new StudentMapper(entityManager);

  @Test
  void maps_entity_to_model() {
    var userHei = JUserHei.builder().id(UUID.randomUUID()).build();
    var entity =
        JStudent.builder()
            .id(UUID.randomUUID())
            .userHei(userHei)
            .studentNumber("STU-1")
            .entryDate(LocalDate.of(2024, 9, 1))
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.userId()).isEqualTo(userHei.getId());
    assertThat(model.studentNumber()).isEqualTo("STU-1");
    assertThat(model.entryDate()).isEqualTo(LocalDate.of(2024, 9, 1));
  }

  @Test
  void maps_model_to_entity() {
    var userId = UUID.randomUUID();
    var userHei = JUserHei.builder().id(userId).build();
    when(entityManager.getReference(JUserHei.class, userId)).thenReturn(userHei);

    var model =
        school.hei.exam.model.Student.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .studentNumber("STU-2")
            .entryDate(LocalDate.of(2024, 9, 1))
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getUserHei()).isEqualTo(userHei);
    assertThat(entity.getStudentNumber()).isEqualTo("STU-2");
  }

  @Test
  void maps_lists() {
    var userHei = JUserHei.builder().id(UUID.randomUUID()).build();
    var e1 = JStudent.builder().id(UUID.randomUUID()).userHei(userHei).build();
    var e2 = JStudent.builder().id(UUID.randomUUID()).userHei(userHei).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);
  }
}
