package school.hei.exam.endpoint.rest.controller;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.exam.model.Track;
import school.hei.exam.service.CourseOfferingService;
import school.hei.exam.service.GraduateExportService;
import school.hei.exam.service.PromotionResultService;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

  private final GraduateExportService graduateExportService;
  private final PromotionResultService promotionResultService;
  private final CourseOfferingService courseOfferingService;

  @GetMapping("/promotions/{promotion}/graduates.xlsx")
  public ResponseEntity<ByteArrayResource> downloadGraduates(
      @PathVariable String promotion, @RequestParam Track track) {
    byte[] xlsx = graduateExportService.generateGraduatesXlsx(promotion, track);

    return ResponseEntity.ok()
        .contentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=diplomes_" + promotion + "_" + track + ".xlsx")
        .body(new ByteArrayResource(xlsx));
  }

  @GetMapping("/promotions/{promotion}/results")
  public Object getPromotionResults(@PathVariable String promotion) {
    return promotionResultService.getResults(promotion);
  }

  @GetMapping("/promotions/{promotion}/results/{track}")
  public Object getPromotionResultsByTrack(
      @PathVariable String promotion, @PathVariable Track track) {
    return promotionResultService.getResultsByTrack(promotion, track);
  }

  @PostMapping("/course-offerings/{courseOfferingId}/teachers/{teacherId}")
  public ResponseEntity<Void> assignTeacher(
      @PathVariable UUID courseOfferingId, @PathVariable UUID teacherId) {
    courseOfferingService.assignTeacher(courseOfferingId, teacherId);
    return ResponseEntity.noContent().build();
  }
}
