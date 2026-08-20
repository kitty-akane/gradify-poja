package school.hei.exam.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JStudent;

public interface StudentRepository extends JpaRepository<JStudent, UUID> {
  boolean existsByIdAndUserHei_Id(UUID studentId, UUID userHeiId);
}
