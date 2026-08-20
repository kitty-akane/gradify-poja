package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.hei.exam.model.Level;
import school.hei.exam.repository.model.JGroup;

public interface GroupRepository extends JpaRepository<JGroup, UUID> {
  @Query(
      "select distinct group.academicYear from JGroup group where group.level = :level order by"
          + " group.academicYear desc")
  List<String> findDistinctAcademicYearByLevel(@Param("level") Level level);
}
