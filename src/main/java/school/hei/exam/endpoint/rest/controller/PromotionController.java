package school.hei.exam.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import school.hei.exam.endpoint.rest.model.GraduateDto;
import school.hei.exam.endpoint.rest.model.PromotionDto;
import school.hei.exam.endpoint.rest.model.PromotionResultsDto;
import school.hei.exam.file.excel.GraduatesExcelExporter;
import school.hei.exam.service.PromotionService;

@RestController
@AllArgsConstructor
public class PromotionController {

  private final PromotionService promotionService;
  private final GraduatesExcelExporter graduatesExcelExporter;

  @GetMapping("/promotions")
  public List<PromotionDto> getPromotions() {
    return promotionService.listPromotions();
  }

  @GetMapping("/promotions/{promotion}/graduates")
  public List<GraduateDto> getGraduates(@PathVariable String promotion) {
    return promotionService.graduates(promotion);
  }

