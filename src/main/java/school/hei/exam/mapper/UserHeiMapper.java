package school.hei.exam.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import school.hei.exam.model.UserHei;
import school.hei.exam.repository.model.JUserHei;

@Component
public class UserHeiMapper {
  public List<UserHei> toModel(List<JUserHei> userHei) {
    return userHei.stream().map(this::toModel).toList();
  }

  public UserHei toModel(JUserHei userHei) {
    return UserHei.builder()
        .id(userHei.getId())
        .firstName(userHei.getFirstName())
        .lastName(userHei.getLastName())
        .email(userHei.getEmail())
        .phoneNumber(userHei.getPhoneNumber())
        .address(userHei.getAddress())
        .role(userHei.getRole())
        .build();
  }

  public List<JUserHei> toEntity(List<UserHei> userHei) {
    return userHei.stream().map(this::toEntity).toList();
  }

  public JUserHei toEntity(UserHei userHei) {
    return JUserHei.builder()
        .id(userHei.id())
        .firstName(userHei.firstName())
        .lastName(userHei.lastName())
        .email(userHei.email())
        .phoneNumber(userHei.phoneNumber())
        .address(userHei.address())
        .role(userHei.role())
        .build();
  }
}
