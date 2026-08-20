package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import school.hei.exam.model.Track;

@Entity
@Table(
    name = "course_offering",
    uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "group_id", "academic_year"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JCourseOffering {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  private JCourse course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id", nullable = false)
  private JGroup group;

  @Column(name = "academic_year", nullable = false)
  private String academicYear;

  @Enumerated(EnumType.STRING)
  @Column(name = "track")
  private Track track;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_offering_teacher",
      joinColumns = @JoinColumn(name = "course_offering_id"),
      inverseJoinColumns = @JoinColumn(name = "teacher_id"))
  @Builder.Default
  private List<JTeacher> teachers = new ArrayList<>();

  @OneToMany(mappedBy = "courseOffering", fetch = FetchType.LAZY)
  @Builder.Default
  private List<JExam> exams = new ArrayList<>();
}
