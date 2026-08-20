package school.hei.exam.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.model.Level;
import school.hei.exam.repository.GroupRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PromotionListService {

  private final GroupRepository groupRepository;

  public List<String> listAllPromotions() {
    return groupRepository.findDistinctAcademicYearByLevel(Level.L3);
  }
}
