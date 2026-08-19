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
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.ExamMapper;
import school.hei.exam.model.Exam;
import school.hei.exam.repository.ExamRepository;
import school.hei.exam.repository.model.JCourseOffering;
import school.hei.exam.repository.model.JExam;

@RestController
@RequestMapping("/course-offerings/{offeringId}/exams")
@AllArgsConstructor
public class ExamController {

  private final ExamRepository examRepository;
  private final ExamMapper examMapper;
  private final EntityManager entityManager;

  @GetMapping
  public List<Exam> getExamsByOffering(@PathVariable UUID offeringId) {
    return examMapper.toModel(examRepository.findByCourseOfferingId(offeringId));
  }

