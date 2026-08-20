package school.hei.exam.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.exam.repository.model.JUserHei;

@Repository
public interface UserHeiRepository extends JpaRepository<JUserHei, UUID> {
  Optional<JUserHei> findByEmail(String email);
}
