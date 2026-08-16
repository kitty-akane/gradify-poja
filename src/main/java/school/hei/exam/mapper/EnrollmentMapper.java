package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Enrollment;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JStudent;

@Component
@AllArgsConstructor
public class EnrollmentMapper {
  private final EntityManager entityManager;

  public List<Enrollment> toModel(List<JEnrollment> enrollments) {
    return enrollments.stream().map(this::toModel).toList();
  }

  public Enrollment toModel(JEnrollment enrollment) {
    return Enrollment.builder()
        .id(enrollment.getId())
        .studentId(enrollment.getStudent().getId())
        .groupId(enrollment.getGroup().getId())
        .academicYear(enrollment.getAcademicYear())
        .level(enrollment.getLevel())
        .track(enrollment.getTrack())
        .build();
  }

  public List<JEnrollment> toEntity(List<Enrollment> enrollments) {
    return enrollments.stream().map(this::toEntity).toList();
  }

  public JEnrollment toEntity(Enrollment enrollment) {
    JStudent student = entityManager.getReference(JStudent.class, enrollment.studentId());
    JGroup group = entityManager.getReference(JGroup.class, enrollment.groupId());
    return JEnrollment.builder()
        .id(enrollment.id())
        .student(student)
        .group(group)
        .academicYear(enrollment.academicYear())
        .level(enrollment.level())
        .track(enrollment.track())
        .build();
  }
}
