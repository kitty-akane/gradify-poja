package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JGroup;

public interface GroupRepository extends JpaRepository<JGroup, UUID> {
  @Query(
      "select distinct g.academicYear from JGroup g where g.level = :level order by"
          + " g.academicYear desc")
  List<String> findDistinctAcademicYearByLevel(Level level);

  List<JGroup> findByAcademicYearAndLevel(String academicYear, Level level);

  List<JGroup> findByAcademicYear(String academicYear);

  List<JGroup> findByLevel(Level level);
}
