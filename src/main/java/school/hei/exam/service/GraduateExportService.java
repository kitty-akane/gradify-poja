package school.hei.exam.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.exam.exception.ExportException;
import school.hei.exam.model.PromotionResultLine;
import school.hei.exam.model.Track;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GraduateExportService {

  private final PromotionResultService promotionResultService;

  public byte[] generateGraduatesXlsx(String promotion, Track track) {
    List<PromotionResultLine> graduates =
        promotionResultService.getResultsByTrack(promotion, track).stream()
            .filter(PromotionResultLine::validated)
            .toList();

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet =
          workbook.createSheet("Diplômés " + promotion + " - " + track);

      CellStyle headerStyle = workbook.createCellStyle();
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerStyle.setFont(headerFont);

      Row header = sheet.createRow(0);

      String[] columns = {
        "Rang", "STD", "Nom", "Prénom", "Moyenne générale"
      };

      for (int i = 0; i < columns.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(columns[i]);
        cell.setCellStyle(headerStyle);
      }

      int rowIndex = 1;

      for (PromotionResultLine line : graduates) {
        Row row = sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue(line.rank());
        row.createCell(1).setCellValue(line.studentNumber());
        row.createCell(2).setCellValue(line.lastName());
        row.createCell(3).setCellValue(line.firstName());
        row.createCell(4).setCellValue(line.generalAverage().doubleValue());
      }

      for (int i = 0; i < columns.length; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(out);
      return out.toByteArray();

    } catch (IOException e) {
      throw new ExportException(
          "Erreur lors de la génération du fichier Excel");
    }
  }
}
