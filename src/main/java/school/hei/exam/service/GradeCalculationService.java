package school.hei.exam.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.exception.NotFoundException;
import school.hei.exam.model.Track;
import school.hei.exam.repository.CourseOfferingRepository;
import school.hei.exam.repository.EnrollmentRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JExam;
import school.hei.exam.repository.model.JGrade;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GradeCalculationService {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseOfferingRepository courseOfferingRepository;
  private final GradeRepository gradeRepository;

  public BigDecimal computeCourseOfferingAverage(UUID studentId, JCourseOffering courseOffering) {

    List<JGrade> studentGrades =
        gradeRepository.findByExam_CourseOffering_Id(courseOffering.getId()).stream()
            .filter(g -> g.getStudent().getId().equals(studentId))
            .toList();

    BigDecimal weightedSum = BigDecimal.ZERO;
    BigDecimal totalCoefficient = BigDecimal.ZERO;

    for (JExam exam : courseOffering.getExams()) {
      BigDecimal coefficient = exam.getCoefficient();

      BigDecimal gradeValue =
          studentGrades.stream()
              .filter(g -> g.getExam().getId().equals(exam.getId()))
              .map(JGrade::getValue)
              .findFirst()
              .orElse(null);

      if (gradeValue == null) continue;

      weightedSum = weightedSum.add(gradeValue.multiply(coefficient));
      totalCoefficient = totalCoefficient.add(coefficient);
    }

    if (totalCoefficient.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    return weightedSum.divide(totalCoefficient, 2, RoundingMode.HALF_UP);
  }

  public List<JCourseOffering> getCourseOfferingsForStudentYear(
      UUID studentId, String academicYear) {

    JEnrollment enrollment =
        enrollmentRepository.findByStudent_IdOrderByAcademicYearAsc(studentId).stream()
            .filter(e -> e.getAcademicYear().equals(academicYear))
            .findFirst()
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Aucune inscription pour l'étudiant " + studentId + " en " + academicYear));

    Track studentTrack = enrollment.getTrack();

    return courseOfferingRepository
        .findByGroup_IdAndAcademicYear(enrollment.getGroup().getId(), academicYear)
        .stream()
        .filter(offering -> isVisibleForTrack(offering, studentTrack))
        .toList();
  }

  private boolean isVisibleForTrack(JCourseOffering offering, Track studentTrack) {
    Track offeringTrack = offering.getTrack();

    return offeringTrack == null
        || offeringTrack == Track.TRONC_COMMUN
        || offeringTrack == studentTrack;
  }

  public BigDecimal computeYearGeneralAverage(UUID studentId, String academicYear) {
    return weightedAverageByCredits(
        studentId, getCourseOfferingsForStudentYear(studentId, academicYear));
  }

  public boolean hasValidatedAllCoursesForYear(UUID studentId, String academicYear) {
    return getCourseOfferingsForStudentYear(studentId, academicYear).stream()
        .allMatch(o -> computeCourseOfferingAverage(studentId, o).compareTo(BigDecimal.TEN) >= 0);
  }

  public BigDecimal computeCursusAverage(UUID studentId) {
    List<String> academicYears = distinctAcademicYears(studentId);

    List<JCourseOffering> allOfferings =
        academicYears.stream()
            .flatMap(year -> getCourseOfferingsForStudentYear(studentId, year).stream())
            .toList();

    return weightedAverageByCredits(studentId, allOfferings);
  }

  public boolean isGraduate(UUID studentId) {
    List<String> academicYears = distinctAcademicYears(studentId);

    boolean allValidated =
        academicYears.stream().allMatch(year -> hasValidatedAllCoursesForYear(studentId, year));

    return allValidated && computeCursusAverage(studentId).compareTo(BigDecimal.TEN) >= 0;
  }

  private List<String> distinctAcademicYears(UUID studentId) {
    return enrollmentRepository.findByStudent_IdOrderByAcademicYearAsc(studentId).stream()
        .map(JEnrollment::getAcademicYear)
        .distinct()
        .toList();
  }

  private BigDecimal weightedAverageByCredits(UUID studentId, List<JCourseOffering> offerings) {

    BigDecimal weightedSum = BigDecimal.ZERO;
    BigDecimal totalCredits = BigDecimal.ZERO;

    for (JCourseOffering offering : offerings) {
      BigDecimal credits = BigDecimal.valueOf(offering.getCourse().getCredits());

      BigDecimal average = computeCourseOfferingAverage(studentId, offering);

      weightedSum = weightedSum.add(average.multiply(credits));
      totalCredits = totalCredits.add(credits);
    }

    if (totalCredits.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    return weightedSum.divide(totalCredits, 2, RoundingMode.HALF_UP);
  }
}
