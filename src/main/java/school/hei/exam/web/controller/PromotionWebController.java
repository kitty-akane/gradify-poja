package school.hei.exam.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import school.hei.exam.service.PromotionListService;

@Controller
@RequestMapping("/thymeleaf")
@AllArgsConstructor
public class PromotionWebController {

  private final PromotionListService promotionListService;

  @GetMapping("/promotions")
  public String listPromotions(Model model) {
    model.addAttribute("promotions", promotionListService.listAllPromotions());
    return "promotions";
  }
}
