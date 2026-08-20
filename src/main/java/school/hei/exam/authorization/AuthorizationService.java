package school.hei.exam.authorization;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.exam.repository.CourseOfferingRepository;
import school.hei.exam.repository.ExamRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.StudentRepository;
import school.hei.exam.security.SecurityUtils;

@Service
@AllArgsConstructor
public class AuthorizationService {
  private final StudentRepository studentRepository;
  private final CourseOfferingRepository courseOfferingRepository;
  private final ExamRepository examRepository;
  private final GradeRepository gradeRepository;

  public boolean isAdmin() {
    return SecurityUtils.hasRole("ADMIN");
  }

  public boolean isTeacher() {
    return SecurityUtils.hasRole("TEACHER");
  }

  public boolean isSelfStudentOrAdmin(UUID studentId) {
    if (isAdmin()) return true;
    if (studentId == null) return false;
    return studentRepository.existsByIdAndUserHei_Id(studentId, SecurityUtils.currentUserId());
  }

  public boolean teachesCourseOfferingOrAdmin(UUID courseOfferingId) {
    if (isAdmin()) return true;
    if (courseOfferingId == null) return false;
    return courseOfferingRepository.existsByIdAndTeachers_UserHei_Id(
        courseOfferingId, SecurityUtils.currentUserId());
  }

  public boolean teachesExamOrAdmin(UUID examId) {
    if (isAdmin()) return true;
    if (examId == null) return false;
    return examRepository.existsByIdAndCourseOffering_Teachers_UserHei_Id(
        examId, SecurityUtils.currentUserId());
  }

  public boolean canModifyGradeOrAdmin(UUID gradeId) {
    if (isAdmin()) return true;
    if (gradeId == null) return false;
    return gradeRepository.existsByIdAndExam_CourseOffering_Teachers_UserHei_Id(
        gradeId, SecurityUtils.currentUserId());
  }
}
