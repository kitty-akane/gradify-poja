package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "grade_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JGradeHistory {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "grade_id", nullable = false)
  private JGrade grade;

  @Column(name = "old_value")
  private BigDecimal oldValue;

  @Column(name = "new_value")
  private BigDecimal newValue;

  @Column(length = 500, nullable = false)
  private String reason;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by_id", nullable = false)
  private JUserHei modifiedById;

  @Column(name = "modified_at")
  private Instant modifiedAt;
}
