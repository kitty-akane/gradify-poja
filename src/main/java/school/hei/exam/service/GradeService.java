package school.hei.exam.service;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.exception.NotFoundException;
import school.hei.exam.mapper.GradeMapper;
import school.hei.exam.model.Grade;
import school.hei.exam.repository.GradeHistoryRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JGradeHistory;
import school.hei.exam.repository.model.JUserHei;

@Service
@AllArgsConstructor
@Transactional
public class GradeService {

  private final GradeRepository gradeRepository;
  private final GradeHistoryRepository gradeHistoryRepository;
  private final GradeMapper gradeMapper;
  private final EntityManager entityManager;

  @Transactional(readOnly = true)
  public List<Grade> getGradesForStudent(UUID studentId, String academicYear) {
    return gradeMapper.toModel(
        gradeRepository.findByStudent_IdAndExam_CourseOffering_AcademicYear(
            studentId, academicYear));
  }

  @Transactional(readOnly = true)
  public List<Grade> getGradesForCourseOffering(UUID courseOfferingId) {
    return gradeMapper.toModel(gradeRepository.findByExam_CourseOffering_Id(courseOfferingId));
  }

  @Transactional(readOnly = true)
  public List<Grade> getGradesForTeacher(UUID teacherId) {
    return gradeMapper.toModel(gradeRepository.findByExam_CourseOffering_Teachers_Id(teacherId));
  }

  public Grade createGrade(Grade grade) {
    return gradeMapper.toModel(gradeRepository.save(gradeMapper.toEntity(grade)));
  }

  public Grade updateGrade(UUID gradeId, BigDecimal newValue, String reason, UUID modifiedById) {

    JGrade grade =
        gradeRepository
            .findById(gradeId)
            .orElseThrow(() -> new NotFoundException("Note introuvable : " + gradeId));

    BigDecimal oldValue = grade.getValue();

    JUserHei author = entityManager.getReference(JUserHei.class, modifiedById);

    gradeHistoryRepository.save(
        JGradeHistory.builder()
            .grade(grade)
            .oldValue(oldValue)
            .newValue(newValue)
            .reason(reason)
            .modifiedById(author)
            .modifiedAt(Instant.now())
            .build());

    grade.setValue(newValue);

    return gradeMapper.toModel(gradeRepository.save(grade));
  }
}
