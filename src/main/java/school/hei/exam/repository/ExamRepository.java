package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JExam;

public interface ExamRepository extends JpaRepository<JExam, UUID> {
  boolean existsByIdAndCourseOffering_Teachers_UserHei_Id(UUID examId, UUID userHeiId);

  List<JExam> findByCourseOffering_Id(UUID courseOfferingId);
}
