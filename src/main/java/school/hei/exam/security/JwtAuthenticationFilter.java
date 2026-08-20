package school.hei.exam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String HEADER = "Authorization";
  private static final String PREFIX = "Bearer ";
  private static final String COOKIE_NAME = "jwt";

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = extractTokenFromHeader(request);
    if (token == null) {
      token = extractTokenFromCookie(request);
    }

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      String email = jwtService.extractEmail(token);
      if (email != null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtService.isTokenValid(token, userDetails)) {
          var authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader(HEADER);
    if (header != null && header.startsWith(PREFIX)) {
      return header.substring(PREFIX.length());
    }
    return null;
  }

  private String extractTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) return null;
    return Arrays.stream(request.getCookies())
        .filter(c -> COOKIE_NAME.equals(c.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }
}
