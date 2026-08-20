package school.hei.exam.service.event;

import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import school.hei.exam.endpoint.event.model.SendTranscriptRequested;
import school.hei.exam.mail.Email;
import school.hei.exam.mail.Mailer;
import school.hei.exam.service.TranscriptService;

@Service
@AllArgsConstructor
public class SendTranscriptRequestedService implements Consumer<SendTranscriptRequested> {

  private final TranscriptService transcriptService;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(SendTranscriptRequested event) {
    var result = transcriptService.generateAndUploadTranscript(event.getStudentId());

    var recipientAddress = new InternetAddress(result.recipientEmail());

    var email =
        new Email(
            recipientAddress,
            List.of(),
            List.of(),
            "Votre relevé de notes HEI",
            "Bonjour "
                + result.studentFullName()
                + ",\n\nVotre relevé de notes est disponible via ce lien (valide 7 jours) :\n"
                + result.presignedUrl()
                + "\n\nCordialement,\nHEI",
            List.of());

    mailer.accept(email);
  }
}
