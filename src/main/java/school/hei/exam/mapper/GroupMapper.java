package school.hei.exam.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import school.hei.exam.model.Group;
import school.hei.exam.repository.model.JGroup;

@Component
public class GroupMapper {
  public List<Group> toModel(List<JGroup> groups) {
    return groups.stream().map(this::toModel).toList();
  }

  public Group toModel(JGroup group) {
    return Group.builder()
        .id(group.getId())
        .ref(group.getRef())
        .level(group.getLevel())
        .academicYear(group.getAcademicYear())
        .build();
  }

  public List<JGroup> toEntity(List<Group> groups) {
    return groups.stream().map(this::toEntity).toList();
  }

  public JGroup toEntity(Group group) {
    return JGroup.builder()
        .id(group.id())
        .ref(group.ref())
        .level(group.level())
        .academicYear(group.academicYear())
        .build();
  }
}
