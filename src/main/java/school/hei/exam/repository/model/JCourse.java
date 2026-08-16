package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JCourse {
  @Id @GeneratedValue @UuidGenerator private UUID id;
  private String ref;
  private String title;
  private int credits;

  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JCourseOffering> courseOfferings = new ArrayList<>();
}
