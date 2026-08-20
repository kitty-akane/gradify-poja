package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JGroup;

public interface GroupRepository extends JpaRepository<JGroup, UUID> {
  List<JGroup> findByAcademicYearAndLevel(String academicYear, Level level);

  List<JGroup> findByAcademicYear(String academicYear);

  List<JGroup> findByLevel(Level level);
}
