package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "exam")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JExam {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offering_id", nullable = false)
  private JCourseOffering courseOffering;

  private String label;

  @Column(name = "exam_date")
  private Instant examDate;

  private BigDecimal coefficient;

  @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JGrade> grades = new ArrayList<>();
}
