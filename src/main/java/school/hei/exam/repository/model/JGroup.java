package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import school.hei.exam.model.Level;

@Entity
@Table(name = "group_hei")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JGroup {
  @Id @GeneratedValue @UuidGenerator private UUID id;
  private String ref;

  @Enumerated(EnumType.STRING)
  private Level level;

  @Column(name = "academic_year")
  private String academicYear;

  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JEnrollment> enrollments = new ArrayList<>();

  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JCourseOffering> courseOfferings = new ArrayList<>();
}
