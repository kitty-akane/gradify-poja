package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JCourseOffering;

public interface CourseOfferingRepository extends JpaRepository<JCourseOffering, UUID> {
  boolean existsByIdAndTeachers_UserHei_Id(UUID courseOfferingId, UUID userHeiId);

  List<JCourseOffering> findByGroup_IdAndAcademicYear(UUID groupId, String academicYear);
}
