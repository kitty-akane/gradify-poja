package school.hei.exam.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JEnrollment;

public interface EnrollmentRepository extends JpaRepository<JEnrollment, UUID> {}
