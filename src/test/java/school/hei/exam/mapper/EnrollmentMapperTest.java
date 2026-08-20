package school.hei.exam.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import school.hei.exam.model.Level;
import school.hei.exam.model.Track;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JStudent;

class EnrollmentMapperTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final EnrollmentMapper mapper = new EnrollmentMapper(entityManager);

  @Test
  void maps_entity_to_model() {
    var student = JStudent.builder().id(UUID.randomUUID()).build();
    var group = JGroup.builder().id(UUID.randomUUID()).build();
    var entity =
        JEnrollment.builder()
            .id(UUID.randomUUID())
            .student(student)
            .group(group)
            .academicYear("2030-2031")
            .level(Level.L1)
            .track(Track.TRONC_COMMUN)
            .build();

    var model = mapper.toModel(entity);

    assertThat(model.id()).isEqualTo(entity.getId());
    assertThat(model.studentId()).isEqualTo(student.getId());
    assertThat(model.groupId()).isEqualTo(group.getId());
    assertThat(model.academicYear()).isEqualTo("2030-2031");
    assertThat(model.level()).isEqualTo(Level.L1);
    assertThat(model.track()).isEqualTo(Track.TRONC_COMMUN);
  }

  @Test
  void maps_model_to_entity() {
    var studentId = UUID.randomUUID();
    var groupId = UUID.randomUUID();
    var student = JStudent.builder().id(studentId).build();
    var group = JGroup.builder().id(groupId).build();
    when(entityManager.getReference(JStudent.class, studentId)).thenReturn(student);
    when(entityManager.getReference(JGroup.class, groupId)).thenReturn(group);

    var model =
        school.hei.exam.model.Enrollment.builder()
            .id(UUID.randomUUID())
            .studentId(studentId)
            .groupId(groupId)
            .academicYear("2030-2031")
            .level(Level.L2)
            .track(Track.TN)
            .build();

    var entity = mapper.toEntity(model);

    assertThat(entity.getId()).isEqualTo(model.id());
    assertThat(entity.getStudent()).isEqualTo(student);
    assertThat(entity.getGroup()).isEqualTo(group);
    assertThat(entity.getTrack()).isEqualTo(Track.TN);
  }

  @Test
  void maps_lists() {
    var student = JStudent.builder().id(UUID.randomUUID()).build();
    var group = JGroup.builder().id(UUID.randomUUID()).build();
    var e1 = JEnrollment.builder().id(UUID.randomUUID()).student(student).group(group).build();
    var e2 = JEnrollment.builder().id(UUID.randomUUID()).student(student).group(group).build();

    var models = mapper.toModel(List.of(e1, e2));
    assertThat(models).hasSize(2);
  }
}
