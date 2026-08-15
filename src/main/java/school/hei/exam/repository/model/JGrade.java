package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(
    name = "grade",
    uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "student_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JGrade {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id", nullable = false)
  private JExam exam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private JStudent student;

  private BigDecimal value;

  @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JGradeHistory> histories = new ArrayList<>();
}
