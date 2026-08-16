package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JStudent {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_hei_id", nullable = false, unique = true)
  private JUserHei userHei;

  @Column(name = "student_number", unique = true, nullable = false)
  private String studentNumber;

  @Column(name = "entry_date")
  private LocalDate entryDate;

  @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JEnrollment> enrollments = new ArrayList<>();

  @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JGrade> grades = new ArrayList<>();
}
