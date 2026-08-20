package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JGradeHistory;
import school.hei.exam.repository.model.JUserHei;

class GradeHistoryMapperTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final GradeHistoryMapper mapper = new GradeHistoryMapper(entityManager);

  @Test
  void maps_entity_to_model() {
    var grade = JGrade.builder().id(UUID.randomUUID()).build();
    var modifiedBy = JUserHei.builder().id(UUID.randomUUID()).build();
    var now = Instant.now();
    var entity =
        JGradeHistory.builder()
            .id(UUID.randomUUID())
            .grade(grade)
            .oldValue(new BigDecimal("10"))
            .newValue(new BigDecimal("15"))
            .reason("correction")
            .modifiedById(modifiedBy)
            .modifiedAt(now)
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.gradeId()).isEqualTo(grade.getId());
    assertThat(model.oldValue()).isEqualByComparingTo("10");
    assertThat(model.newValue()).isEqualByComparingTo("15");
    assertThat(model.reason()).isEqualTo("correction");
    assertThat(model.modifiedById()).isEqualTo(modifiedBy.getId());
    assertThat(model.modifiedAt()).isEqualTo(now);
  }

  @Test
  void maps_model_to_entity() {
    var gradeId = UUID.randomUUID();
    var modifiedById = UUID.randomUUID();
    var grade = JGrade.builder().id(gradeId).build();
    var userHei = JUserHei.builder().id(modifiedById).build();
    when(entityManager.getReference(JGrade.class, gradeId)).thenReturn(grade);
    when(entityManager.getReference(JUserHei.class, modifiedById)).thenReturn(userHei);

    var model =
        school.hei.exam.model.GradeHistory.builder()
            .id(UUID.randomUUID())
            .gradeId(gradeId)
            .oldValue(new BigDecimal("8"))
            .newValue(new BigDecimal("12"))
            .reason("re-grade")
            .modifiedById(modifiedById)
            .modifiedAt(Instant.now())
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getGrade()).isEqualTo(grade);
    assertThat(entity.getModifiedById()).isEqualTo(userHei);
    assertThat(entity.getReason()).isEqualTo("re-grade");
  }

  @Test
  void maps_lists() {
    var grade = JGrade.builder().id(UUID.randomUUID()).build();
    var userHei = JUserHei.builder().id(UUID.randomUUID()).build();
    var e1 =
        JGradeHistory.builder().id(UUID.randomUUID()).grade(grade).modifiedById(userHei).build();
    var e2 =
        JGradeHistory.builder().id(UUID.randomUUID()).grade(grade).modifiedById(userHei).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);
  }
}
