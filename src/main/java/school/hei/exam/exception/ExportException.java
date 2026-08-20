package school.hei.exam.exception;

import org.springframework.http.HttpStatus;

public class ExportException extends ApiException {
  public ExportException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
