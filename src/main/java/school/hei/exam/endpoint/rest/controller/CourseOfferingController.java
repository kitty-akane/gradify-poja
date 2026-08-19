package school.hei.exam.endpoint.rest.controller;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.CourseOfferingMapper;
import school.hei.exam.model.CourseOffering;
import school.hei.exam.repository.CourseOfferingRepository;
import school.hei.exam.repository.model.JCourse;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JGroup;
import school.hei.exam.repository.model.JTeacher;

@RestController
@RequestMapping("/course-offerings")
@AllArgsConstructor
public class CourseOfferingController {

  private final CourseOfferingRepository courseOfferingRepository;
  private final CourseOfferingMapper courseOfferingMapper;
  private final EntityManager entityManager;

  @GetMapping
  public List<CourseOffering> getCourseOfferings(
      @RequestParam(required = false) UUID groupId,
      @RequestParam(required = false) String academicYear,
      @RequestParam(required = false) UUID teacherId) {
    List<JCourseOffering> found;
    if (groupId != null && academicYear != null) {
      found = courseOfferingRepository.findByGroupIdAndAcademicYear(groupId, academicYear);
    } else if (groupId != null) {
      found = courseOfferingRepository.findByGroupId(groupId);
    } else if (academicYear != null) {
      found = courseOfferingRepository.findByAcademicYear(academicYear);
    } else if (teacherId != null) {
      found = courseOfferingRepository.findByTeachers_Id(teacherId);
    } else {
      found = courseOfferingRepository.findAll();
    }
    return courseOfferingMapper.toModel(found);
  }

