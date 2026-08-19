package school.hei.exam.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import school.hei.exam.conf.AbstractIT;
import school.hei.exam.endpoint.rest.model.GraduateDto;
import school.hei.exam.endpoint.rest.model.PromotionDto;
import school.hei.exam.endpoint.rest.model.PromotionResultsDto;
import school.hei.exam.model.Level;

class PromotionControllerIT extends AbstractIT {

  @Test
  void graduates_json_matches_students_enrolled_in_l3_for_that_year() {
    var promotion = "2031-2032";
    var course = createCourse();
    var l3Group = createGroup(Level.L3, promotion);
    var teacher = createTeacher();
    var offering = createCourseOffering(course, l3Group, promotion, teacher);
    var exam = createExam(offering, "Soutenance");

    var graduate = createStudent();
    enroll(graduate, l3Group, promotion, Level.L3);
    createGrade(exam, graduate, new BigDecimal("16"));

    var notGraduating = createStudent();
    enroll(notGraduating, createGroup(Level.L1, promotion), promotion, Level.L1);

    var response =
        restTemplate.exchange(
            "/promotions/" + promotion + "/graduates", GET, HttpEntity.EMPTY, GraduateDto[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()[0].studentId()).isEqualTo(graduate.getId());
    assertThat(response.getBody()[0].averageGrade()).isEqualByComparingTo("16.00");
  }

  @Test
  void export_is_a_real_readable_xlsx_with_the_graduates() {
    var promotion = "2032-2033";
    var l3Group = createGroup(Level.L3, promotion);
    var graduate = createStudent();
    enroll(graduate, l3Group, promotion, Level.L3);

    var response =
        restTemplate.exchange(
            "/promotions/" + promotion + "/graduates/export",
            GET,
            HttpEntity.EMPTY,
            byte[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders().getContentDisposition().getFilename())
        .isEqualTo("diplomes-" + promotion + ".xlsx");

    // real parse-back of the workbook we just downloaded, not a mock
    try (var workbook = new XSSFWorkbook(new ByteArrayInputStream(response.getBody()))) {
      var sheet = workbook.getSheet(promotion);
      assertThat(sheet).isNotNull();
      Row header = sheet.getRow(0);
      assertThat(header.getCell(0).getStringCellValue()).isEqualTo("Numero etudiant");

      Row dataRow = sheet.getRow(1);
      assertThat(dataRow.getCell(0).getStringCellValue()).isEqualTo(graduate.getStudentNumber());
      assertThat(dataRow.getCell(3).getStringCellValue())
          .isEqualTo(graduate.getUserHei().getEmail());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void promotions_list_reports_the_correct_graduate_count() {
    var promotion = "2033-2034";
    var l3Group = createGroup(Level.L3, promotion);
    enroll(createStudent(), l3Group, promotion, Level.L3);
    enroll(createStudent(), l3Group, promotion, Level.L3);

    var response =
        restTemplate.exchange("/promotions", GET, HttpEntity.EMPTY, PromotionDto[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var found =
        Arrays.stream(response.getBody())
            .filter(p -> p.academicYear().equals(promotion))
            .collect(Collectors.toList());
    assertThat(found).hasSize(1);
    assertThat(found.get(0).graduateCount()).isEqualTo(2);
  }

  @Test
  void results_cover_the_three_years_of_the_promotion() {
    var promotion = "2034-2035";
    createGroup(Level.L3, promotion);

    var response =
        restTemplate.exchange(
            "/promotions/" + promotion + "/results", GET, HttpEntity.EMPTY, PromotionResultsDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().academicYear()).isEqualTo(promotion);
    assertThat(response.getBody().yearlyAverages()).hasSize(3);
    assertThat(response.getBody().yearlyAverages())
        .extracting(a -> a.level())
        .containsExactly(Level.L1, Level.L2, Level.L3);
  }
}
