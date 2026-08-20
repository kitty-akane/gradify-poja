package school.hei.exam.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JEnrollment;

public interface EnrollmentRepository extends JpaRepository<JEnrollment, UUID> {
  List<JEnrollment> findByStudent_IdOrderByAcademicYearAsc(UUID studentId);

  List<JEnrollment> findByStudent_Id(UUID studentId);

  Optional<JEnrollment> findByStudent_IdAndAcademicYear(UUID studentId, String academicYear);

  List<JEnrollment> findByLevelAndAcademicYear(Level level, String academicYear);
}
