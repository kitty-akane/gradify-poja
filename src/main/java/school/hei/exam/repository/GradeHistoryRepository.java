package school.hei.exam.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JGradeHistory;

public interface GradeHistoryRepository extends JpaRepository<JGradeHistory, UUID> {}
