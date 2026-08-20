package school.hei.exam.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.exception.NotFoundException;
import school.hei.exam.repository.CourseOfferingRepository;
import school.hei.exam.repository.TeacherRepository;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JTeacher;

@Service
@AllArgsConstructor
@Transactional
public class CourseOfferingService {

  private final CourseOfferingRepository courseOfferingRepository;
  private final TeacherRepository teacherRepository;

  public void assignTeacher(UUID courseOfferingId, UUID teacherId) {
    JCourseOffering offering =
        courseOfferingRepository
            .findById(courseOfferingId)
            .orElseThrow(() -> new NotFoundException("Cours introuvable : " + courseOfferingId));

    JTeacher teacher =
        teacherRepository
            .findById(teacherId)
            .orElseThrow(() -> new NotFoundException("Enseignant introuvable : " + teacherId));

    if (!offering.getTeachers().contains(teacher)) {
      offering.getTeachers().add(teacher);
      courseOfferingRepository.save(offering);
    }
  }
}
