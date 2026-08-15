package school.hei.exam.repository.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import school.hei.exam.model.Role;

@Entity
@Table(name = "user_hei")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class JUserHei {
  @Id @GeneratedValue @UuidGenerator private UUID id;
  private String firstName;
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "phone_number", unique = true, nullable = false)
  private String phoneNumber;

  private String address;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;
}
