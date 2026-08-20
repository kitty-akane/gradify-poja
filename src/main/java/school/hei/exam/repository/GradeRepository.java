package school.hei.exam.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JGrade;

public interface GradeRepository extends JpaRepository<JGrade, UUID> {
  List<JGrade> findByExam_Id(UUID examId);

  List<JGrade> findByStudent_Id(UUID studentId);

  Optional<JGrade> findByExam_IdAndStudent_Id(UUID examId, UUID studentId);
}
