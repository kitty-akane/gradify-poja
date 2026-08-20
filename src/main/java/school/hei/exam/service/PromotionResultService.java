package school.hei.exam.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.model.Level;
import school.hei.exam.model.PromotionResultLine;
import school.hei.exam.model.Track;
import school.hei.exam.repository.EnrollmentRepository;
import school.hei.exam.repository.model.JEnrollment;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PromotionResultService {

  private final EnrollmentRepository enrollmentRepository;
  private final GradeCalculationService gradeCalculationService;

  public List<PromotionResultLine> getResults(String promotion) {
    List<JEnrollment> l3Enrollments =
        enrollmentRepository.findByLevelAndAcademicYear(
            Level.L3, promotion);

    return buildRankedLines(l3Enrollments);
  }

  public List<PromotionResultLine> getResultsByTrack(
      String promotion, Track track) {

    List<JEnrollment> l3Enrollments =
        enrollmentRepository.findByLevelAndAcademicYear(Level.L3, promotion)
            .stream()
            .filter(e -> e.getTrack() == track)
            .toList();

    return buildRankedLines(l3Enrollments);
  }

  private List<PromotionResultLine> buildRankedLines(
      List<JEnrollment> enrollments) {

    List<PromotionResultLine> unranked =
        enrollments.stream()
            .map(
                enrollment -> {
                  var student = enrollment.getStudent();

                  BigDecimal average =
                      gradeCalculationService.computeCursusAverage(
                          student.getId());

                  boolean validated =
                      gradeCalculationService.isGraduate(student.getId());

                  return PromotionResultLine.builder()
                      .studentId(student.getId())
                      .studentNumber(student.getStudentNumber())
                      .firstName(student.getUserHei().getFirstName())
                      .lastName(student.getUserHei().getLastName())
                      .track(enrollment.getTrack())
                      .generalAverage(average)
                      .validated(validated)
                      .build();
                })
            .sorted(
                Comparator.comparing(
                        PromotionResultLine::generalAverage)
                    .reversed())
            .toList();

    return IntStream.range(0, unranked.size())
        .mapToObj(
            i -> {
              PromotionResultLine l = unranked.get(i);

              return PromotionResultLine.builder()
                  .rank(i + 1)
                  .studentId(l.studentId())
                  .studentNumber(l.studentNumber())
                  .firstName(l.firstName())
                  .lastName(l.lastName())
                  .track(l.track())
                  .generalAverage(l.generalAverage())
                  .validated(l.validated())
                  .build();
            })
        .toList();
  }
}
