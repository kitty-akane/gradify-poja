package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JGradeHistory;

public interface GradeHistoryRepository extends JpaRepository<JGradeHistory, UUID> {
  List<JGradeHistory> findByGrade_Id(UUID gradeId, Sort sort);
}
