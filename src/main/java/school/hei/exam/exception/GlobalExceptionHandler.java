package school.hei.exam.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import school.hei.exam.exception.model.ExceptionBody;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ExceptionBody> handleApiException(
      ApiException exception, HttpServletRequest status) {
    return ResponseEntity.status(exception.getStatus())
        .body(
            new ExceptionBody(
                exception.getStatus().value(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                status.getPathInfo(),
                Instant.now()));
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ExceptionBody> handleMethodArgumentNotValidOrTypeMismatchException(
      Exception exception, HttpServletRequest status) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ExceptionBody(
                400, "Bad Request", exception.getMessage(), status.getPathInfo(), Instant.now()));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ExceptionBody> handleResponseStatusException(
      ResponseStatusException exception, HttpServletRequest status) {
    return ResponseEntity.status(exception.getStatusCode())
        .body(
            new ExceptionBody(
                exception.getStatusCode().value(),
                exception.getReason(),
                exception.getMessage(),
                status.getPathInfo(),
                Instant.now()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionBody> handleException(
      Exception exception, HttpServletRequest status) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ExceptionBody(
                500,
                "Internal Server Error",
                exception.getMessage(),
                status.getPathInfo(),
                Instant.now()));
  }
}
