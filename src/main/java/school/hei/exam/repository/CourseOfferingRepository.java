package school.hei.exam.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.exam.repository.model.JCourseOffering;

public interface CourseOfferingRepository extends JpaRepository<JCourseOffering, UUID> {
  List<JCourseOffering> findByGroup_IdAndAcademicYear(UUID groupId, String academicYear);

  List<JCourseOffering> findByGroup_Id(UUID groupId);

  List<JCourseOffering> findByAcademicYear(String academicYear);

  List<JCourseOffering> findByTeachers_Id(UUID teacherId);
}
