package school.hei.exam.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.hei.exam.security.JwtService;

@Controller
@RequestMapping("/thymeleaf")
@AllArgsConstructor
public class WebAuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String email,
      @RequestParam String password,
      HttpServletResponse response,
      Model model) {
    try {
      var auth =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(email, password));
      UserDetails userDetails = (UserDetails) auth.getPrincipal();

      boolean isAdmin =
          userDetails.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
      if (!isAdmin) {
        model.addAttribute("error", "Accès réservé aux administrateurs");
        return "login";
      }

      String token = jwtService.generateToken(userDetails);
      jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("jwt", token);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      cookie.setMaxAge(3600);
      response.addCookie(cookie);
      return "redirect:/thymeleaf/promotions";
    } catch (AuthenticationException e) {
      model.addAttribute("error", "Email ou mot de passe incorrect");
      return "login";
    }
  }
}
