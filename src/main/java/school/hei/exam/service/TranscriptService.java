package school.hei.exam.service;

import static java.io.File.createTempFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.exception.NotFoundException;
import school.hei.exam.file.bucket.BucketComponent;
import school.hei.exam.repository.EnrollmentRepository;
import school.hei.exam.repository.StudentRepository;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JStudent;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TranscriptService {

  private final StudentRepository studentRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final GradeCalculationService gradeCalculationService;
  private final TranscriptPdfGenerator pdfGenerator;
  private final BucketComponent bucketComponent;

  @SneakyThrows
  public GeneratedTranscript generateAndUploadTranscript(UUID studentId) {

    JStudent student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new NotFoundException("Étudiant introuvable : " + studentId));

    List<JEnrollment> enrollments =
        enrollmentRepository.findByStudent_IdOrderByAcademicYearAsc(studentId);

    List<TranscriptPdfGenerator.YearTranscript> yearTranscripts =
        enrollments.stream()
            .map(
                enrollment -> {
                  String academicYear = enrollment.getAcademicYear();

                  List<JCourseOffering> offerings =
                      gradeCalculationService.getCourseOfferingsForStudentYear(
                          studentId, academicYear);

                  List<TranscriptPdfGenerator.CourseLine> lines =
                      offerings.stream()
                          .map(
                              o ->
                                  new TranscriptPdfGenerator.CourseLine(
                                      o.getCourse().getRef(),
                                      o.getCourse().getTitle(),
                                      gradeCalculationService.computeCourseOfferingAverage(
                                          studentId, o)))
                          .toList();

                  return new TranscriptPdfGenerator.YearTranscript(
                      academicYear,
                      enrollment.getLevel().name(),
                      lines,
                      gradeCalculationService.computeYearGeneralAverage(studentId, academicYear));
                })
            .toList();

    String fullName =
        student.getUserHei().getFirstName() + " " + student.getUserHei().getLastName();

    byte[] pdfContent =
        pdfGenerator.generate(fullName, student.getStudentNumber(), yearTranscripts);

    String bucketKey = "transcripts/" + studentId + "/" + UUID.randomUUID() + ".pdf";

    File tempFile = createTempFile("transcript-" + studentId, ".pdf");

    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      fos.write(pdfContent);
    }

    bucketComponent.upload(tempFile, bucketKey);

    String presignedUrl = bucketComponent.presign(bucketKey, Duration.ofDays(7)).toString();

    return GeneratedTranscript.builder()
        .recipientEmail(student.getUserHei().getEmail())
        .studentFullName(fullName)
        .presignedUrl(presignedUrl)
        .build();
  }

  @Builder
  public record GeneratedTranscript(
      String recipientEmail, String studentFullName, String presignedUrl) {}
}
