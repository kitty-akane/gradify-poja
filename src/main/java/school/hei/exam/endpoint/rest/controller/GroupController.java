package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.mapper.GroupMapper;
import school.hei.exam.model.Group;
import school.hei.exam.model.Level;
import school.hei.exam.repository.GroupRepository;
import school.hei.exam.repository.model.JGroup;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {

  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;

  @GetMapping
  public List<Group> getGroups(
      @RequestParam(required = false) String academicYear,
      @RequestParam(required = false) Level level) {
    List<JGroup> found;
    if (academicYear != null && level != null) {
      found = groupRepository.findByAcademicYearAndLevel(academicYear, level);
    } else if (academicYear != null) {
      found = groupRepository.findByAcademicYear(academicYear);
    } else if (level != null) {
      found = groupRepository.findByLevel(level);
    } else {
      found = groupRepository.findAll();
    }
    return groupMapper.toModel(found);
  }

  @PostMapping
  public ResponseEntity<Group> createGroup(@RequestBody Group group) {
    var toSave =
        JGroup.builder()
            .ref(group.ref())
            .level(group.level())
            .academicYear(group.academicYear())
            .build();
    var saved = groupRepository.save(toSave);
    return ResponseEntity.status(HttpStatus.CREATED).body(groupMapper.toModel(saved));
  }
}
