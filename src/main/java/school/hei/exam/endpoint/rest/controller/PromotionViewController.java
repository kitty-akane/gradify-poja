package school.hei.exam.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import school.hei.exam.service.PromotionService;

@Controller
@AllArgsConstructor
public class PromotionViewController {

  private final PromotionService promotionService;

  @GetMapping("/admin/promotions")
  public String promotions(Model model) {
    model.addAttribute("promotions", promotionService.listPromotions());
    return "promotions";
  }
}
