package school.hei.exam.endpoint.rest.controller;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.EnrollmentMapper;
import school.hei.exam.mapper.GradeMapper;
import school.hei.exam.mapper.StudentMapper;
import school.hei.exam.model.Enrollment;
import school.hei.exam.model.Grade;
import school.hei.exam.model.Student;
import school.hei.exam.repository.EnrollmentRepository;
import school.hei.exam.repository.GradeRepository;
import school.hei.exam.repository.StudentRepository;
import school.hei.exam.repository.model.JEnrollment;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JStudent;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

  private final StudentRepository studentRepository;
  private final GradeRepository gradeRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final StudentMapper studentMapper;
  private final GradeMapper gradeMapper;
  private final EnrollmentMapper enrollmentMapper;
  private final EntityManager entityManager;

  @GetMapping
  public List<Student> getStudents(@RequestParam(required = false) UUID groupId) {
    var found = groupId != null
        ? studentRepository.findByEnrollments_Group_Id(groupId)
        : studentRepository.findAll();
    return studentMapper.toModel(found);
  }

  @GetMapping("/{studentId}")
  public Student getStudentById(@PathVariable UUID studentId) {
    return studentMapper.toModel(studentRepository.findById(studentId).orElseThrow());
  }

  @GetMapping("/{studentId}/grades")
  public List<Grade> getStudentGrades(@PathVariable UUID studentId) {
    return gradeMapper.toModel(gradeRepository.findByStudentId(studentId));
  }

  @GetMapping("/{studentId}/enrollments")
  public List<Enrollment> getStudentEnrollments(@PathVariable UUID studentId) {
    return enrollmentMapper.toModel(enrollmentRepository.findByStudentId(studentId));
  }

  @PostMapping("/{studentId}/enrollments")
  public ResponseEntity<Enrollment> changeStudentGroup(
      @PathVariable UUID studentId, @RequestBody Enrollment enrollment) {
    var student = entityManager.getReference(JStudent.class, studentId);
    var group = entityManager.getReference(JGroup.class, enrollment.groupId());
    var toSave =
        JEnrollment.builder()
            .student(student)
            .group(group)
            .academicYear(enrollment.academicYear())
            .level(enrollment.level())
            .track(enrollment.track())
            .build();
    var saved = enrollmentRepository.save(toSave);
    return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentMapper.toModel(saved));
  }
}
