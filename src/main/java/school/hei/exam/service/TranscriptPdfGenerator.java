package school.hei.exam.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

@Component
public class TranscriptPdfGenerator {

  public byte[] generate(
      String studentFullName,
      String studentNumber,
      List<YearTranscript> years)
      throws IOException {

    try (PDDocument document = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      for (YearTranscript year : years) {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream cs =
            new PDPageContentStream(document, page)) {

          float y = 750;

          cs.beginText();
          cs.setFont(
              new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
              16);
          cs.newLineAtOffset(50, y);
          cs.showText(
              "Relevé de notes - "
                  + studentFullName
                  + " ("
                  + studentNumber
                  + ")");
          cs.endText();

          y -= 30;

          cs.beginText();
          cs.setFont(
              new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
              13);
          cs.newLineAtOffset(50, y);
          cs.showText(
              "Année : " + year.academicYear() + " - " + year.level());
          cs.endText();

          y -= 25;

          cs.setFont(
              new PDType1Font(Standard14Fonts.FontName.HELVETICA),
              11);

          for (CourseLine line : year.courseLines()) {
            cs.beginText();
            cs.newLineAtOffset(50, y);
            cs.showText(
                line.courseRef()
                    + " - "
                    + line.courseTitle()
                    + " : "
                    + line.average()
                    + "/20");
            cs.endText();

            y -= 18;
          }

          y -= 10;

          cs.beginText();
          cs.setFont(
              new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
              12);
          cs.newLineAtOffset(50, y);
          cs.showText(
              "Moyenne générale : "
                  + year.generalAverage()
                  + "/20");
          cs.endText();
        }
      }

      document.save(out);
      return out.toByteArray();
    }
  }

  public record CourseLine(
      String courseRef,
      String courseTitle,
      BigDecimal average) {}

  public record YearTranscript(
      String academicYear,
      String level,
      List<CourseLine> courseLines,
      BigDecimal generalAverage) {}
}
