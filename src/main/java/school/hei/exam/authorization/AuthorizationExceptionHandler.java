package school.hei.exam.authorization;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorizationExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ade) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            Map.of(
                "timestamp",
                Instant.now().toString(),
                "status",
                403,
                "error",
                "Forbidden",
                "message",
                "Vous n'avez pas les droits pour effectuer cette action"));
  }
}
