package school.hei.exam.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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

  private static final String[] COLUMNS = {"Rang", "STD", "Nom", "Prénom", "Moyenne générale"};

  private final PromotionResultService promotionResultService;

  public byte[] generateGraduatesXlsx(String promotion) {
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      CellStyle headerStyle = buildHeaderStyle(workbook);
      for (Track track : List.of(Track.TN, Track.EL)) {
        writeSheet(workbook, headerStyle, "Diplômés " + track, graduatesFor(promotion, track));
      }
      workbook.write(out);
      return out.toByteArray();
    } catch (IOException e) {
      throw new ExportException("Erreur lors de la génération du fichier Excel");
    }
  }

  public byte[] generateGraduatesXlsx(String promotion, Track track) {
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      CellStyle headerStyle = buildHeaderStyle(workbook);
      writeSheet(
          workbook,
          headerStyle,
          "Diplômés " + promotion + " - " + track,
          graduatesFor(promotion, track));
      workbook.write(out);
      return out.toByteArray();
    } catch (IOException e) {
      throw new ExportException("Erreur lors de la génération du fichier Excel");
    }
  }

  private List<PromotionResultLine> graduatesFor(String promotion, Track track) {
    return promotionResultService.getResultsByTrack(promotion, track).stream()
        .filter(PromotionResultLine::validated)
        .toList();
  }

  private CellStyle buildHeaderStyle(Workbook workbook) {
    CellStyle headerStyle = workbook.createCellStyle();
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);
    return headerStyle;
  }

  private void writeSheet(
      Workbook workbook,
      CellStyle headerStyle,
      String sheetName,
      List<PromotionResultLine> graduates) {
    Sheet sheet = workbook.createSheet(sheetName);
    Row header = sheet.createRow(0);
    for (int i = 0; i < COLUMNS.length; i++) {
      Cell cell = header.createCell(i);
      cell.setCellValue(COLUMNS[i]);
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
    for (int i = 0; i < COLUMNS.length; i++) sheet.autoSizeColumn(i);
  }
}
