package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "teacher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JTeacher {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private JUserHei userHei;

  @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JCourseOffering> courseOfferings = new ArrayList<>();
}
