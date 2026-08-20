package school.hei.exam.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import school.hei.exam.exception.UnauthorizedException;

public class SecurityUtils {
  private SecurityUtils() {}

  public static UUID currentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof CustomUserHeiDetails details)) {
      throw new UnauthorizedException("Aucun utilisateur authentifie dans le contexte");
    }
    return details.getId();
  }

  public static boolean hasRole(String role) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) return false;
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
  }
}
