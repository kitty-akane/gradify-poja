package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JEnrollment;

public interface EnrollmentRepository extends JpaRepository<JEnrollment, UUID> {
  List<JEnrollment> findByStudentIdOrderByAcademicYearAsc(UUID studentId);

  List<JEnrollment> findByLevelAndAcademicYear(Level level, String academicYear);
}
