package school.hei.exam.conf;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import school.hei.exam.model.Level;
import school.hei.exam.model.Role;
import school.hei.exam.model.Track;
import school.hei.exam.repository.CourseOfferingRepository;
import school.hei.exam.repository.CourseRepository;
import school.hei.exam.repository.EnrollmentRepository;
import school.hei.exam.repository.ExamRepository;
import school.hei.exam.repository.GradeHistoryRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.GroupRepository;
import school.hei.exam.repository.StudentRepository;
import school.hei.exam.repository.TeacherRepository;
import school.hei.exam.repository.UserHeiRepository;
import school.hei.exam.repository.model.JCourse;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JExam;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JStudent;
import school.hei.exam.repository.model.JTeacher;
import school.hei.exam.repository.model.JUserHei;

public abstract class AbstractIT extends FacadeIT {

  @Autowired protected TestRestTemplate restTemplate;

  @Autowired protected UserHeiRepository userHeiRepository;
  @Autowired protected StudentRepository studentRepository;
  @Autowired protected TeacherRepository teacherRepository;
  @Autowired protected CourseRepository courseRepository;
  @Autowired protected GroupRepository groupRepository;
  @Autowired protected CourseOfferingRepository courseOfferingRepository;
  @Autowired protected ExamRepository examRepository;
  @Autowired protected GradeRepository gradeRepository;
  @Autowired protected GradeHistoryRepository gradeHistoryRepository;
  @Autowired protected EnrollmentRepository enrollmentRepository;

  @AfterEach
  void cleanDatabase() {
    gradeHistoryRepository.deleteAll();
    gradeRepository.deleteAll();
    examRepository.deleteAll();
    enrollmentRepository.deleteAll();
    courseOfferingRepository.deleteAll();
    teacherRepository.deleteAll();
    studentRepository.deleteAll();
    groupRepository.deleteAll();
    courseRepository.deleteAll();
    userHeiRepository.deleteAll();
  }

  protected JUserHei createUser(Role role) {
    var suffix = UUID.randomUUID().toString().substring(0, 8);
    return userHeiRepository.save(
        JUserHei.builder()
            .firstName("Prenom-" + suffix)
            .lastName("Nom-" + suffix)
            .email(suffix + "@hei.school")
            .phoneNumber("+261" + suffix.hashCode() % 900000000)
            .address("Antananarivo")
            .role(role)
            .build());
  }

  protected JUserHei createAdmin() {
    return createUser(Role.ADMIN);
  }

  protected JStudent createStudent() {
    var user = createUser(Role.STUDENT);
    return studentRepository.save(
        JStudent.builder()
            .userHei(user)
            .studentNumber("STU-" + UUID.randomUUID().toString().substring(0, 8))
            .entryDate(LocalDate.now())
            .build());
  }

  protected JTeacher createTeacher() {
    var user = createUser(Role.TEACHER);
    return teacherRepository.save(JTeacher.builder().userHei(user).build());
  }

  protected JCourse createCourse() {
    return courseRepository.save(
        JCourse.builder()
            .ref("REF-" + UUID.randomUUID().toString().substring(0, 6))
            .title("Cours de test")
            .credits(5)
            .build());
  }

  protected JGroup createGroup(Level level, String academicYear) {
    return groupRepository.save(
        JGroup.builder()
            .ref("GRP-" + UUID.randomUUID().toString().substring(0, 6))
            .level(level)
            .academicYear(academicYear)
            .build());
  }

  protected JCourseOffering createCourseOffering(
      JCourse course, JGroup group, String academicYear, JTeacher... teachers) {
    return courseOfferingRepository.save(
        JCourseOffering.builder()
            .course(course)
            .group(group)
            .academicYear(academicYear)
            .teachers(List.of(teachers))
            .build());
  }

  protected JExam createExam(JCourseOffering offering, String label) {
    return examRepository.save(
        JExam.builder()
            .courseOffering(offering)
            .label(label)
            .examDate(Instant.now())
            .coefficient(BigDecimal.ONE)
            .build());
  }

  protected JGrade createGrade(JExam exam, JStudent student, BigDecimal value) {
    return gradeRepository.save(
        JGrade.builder().exam(exam).student(student).value(value).build());
  }

  protected JEnrollment enroll(JStudent student, JGroup group, String academicYear, Level level) {
    return enrollmentRepository.save(
        JEnrollment.builder()
            .student(student)
            .group(group)
            .academicYear(academicYear)
            .level(level)
            .track(Track.TRONC_COMMUN)
            .build());
  }
}
