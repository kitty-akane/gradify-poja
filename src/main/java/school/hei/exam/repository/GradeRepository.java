package school.hei.exam.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JGrade;

public interface GradeRepository extends JpaRepository<JGrade, UUID> {}
