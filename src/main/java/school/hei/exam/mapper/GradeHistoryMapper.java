package school.hei.exam.mapper;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.exam.model.GradeHistory;
import school.hei.exam.repository.model.JGrade;
import school.hei.exam.repository.model.JGradeHistory;
import school.hei.exam.repository.model.JUserHei;

@Component
@AllArgsConstructor
public class GradeHistoryMapper {
  private final EntityManager entityManager;

  public List<GradeHistory> toModel(List<JGradeHistory> gradeHistories) {
    return gradeHistories.stream().map(this::toModel).toList();
  }

  public GradeHistory toModel(JGradeHistory gradeHistory) {
    return GradeHistory.builder()
        .id(gradeHistory.getId())
        .gradeId(gradeHistory.getGrade().getId())
        .oldValue(gradeHistory.getOldValue())
        .newValue(gradeHistory.getNewValue())
        .reason(gradeHistory.getReason())
        .modifiedById(gradeHistory.getModifiedById().getId())
        .modifiedAt(gradeHistory.getModifiedAt())
        .build();
  }

  public List<JGradeHistory> toEntity(List<GradeHistory> gradeHistories) {
    return gradeHistories.stream().map(this::toEntity).toList();
  }

  public JGradeHistory toEntity(GradeHistory gradeHistory) {
    JGrade grade = entityManager.getReference(JGrade.class, gradeHistory.gradeId());
    JUserHei userHei = entityManager.getReference(JUserHei.class, gradeHistory.modifiedById());
    return JGradeHistory.builder()
        .id(gradeHistory.id())
        .grade(grade)
        .oldValue(gradeHistory.oldValue())
        .newValue(gradeHistory.newValue())
        .reason(gradeHistory.reason())
        .modifiedById(userHei)
        .modifiedAt(gradeHistory.modifiedAt())
        .build();
  }
}
