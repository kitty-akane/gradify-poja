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
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JExam;

class ExamMapperTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final ExamMapper mapper = new ExamMapper(entityManager);

  @Test
  void maps_entity_to_model() {
    var offering = JCourseOffering.builder().id(UUID.randomUUID()).build();
    var now = Instant.now();
    var entity =
        JExam.builder()
            .id(UUID.randomUUID())
            .courseOffering(offering)
            .label("Partiel 1")
            .examDate(now)
            .coefficient(BigDecimal.ONE)
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.offeringId()).isEqualTo(offering.getId());
    assertThat(model.label()).isEqualTo("Partiel 1");
    assertThat(model.examDate()).isEqualTo(now);
    assertThat(model.coefficient()).isEqualByComparingTo(BigDecimal.ONE);
  }

  @Test
  void maps_model_to_entity() {
    var offeringId = UUID.randomUUID();
    var offering = JCourseOffering.builder().id(offeringId).build();
    when(entityManager.getReference(JCourseOffering.class, offeringId)).thenReturn(offering);

    var model =
        school.hei.exam.model.Exam.builder()
            .id(UUID.randomUUID())
            .offeringId(offeringId)
            .label("Partiel 2")
            .examDate(Instant.now())
            .coefficient(BigDecimal.TEN)
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getCourseOffering()).isEqualTo(offering);
    assertThat(entity.getLabel()).isEqualTo("Partiel 2");
  }

  @Test
  void maps_lists() {
    var offering = JCourseOffering.builder().id(UUID.randomUUID()).build();
    var e1 = JExam.builder().id(UUID.randomUUID()).courseOffering(offering).build();
    var e2 = JExam.builder().id(UUID.randomUUID()).courseOffering(offering).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);
  }
}
