package school.hei.exam.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import school.hei.exam.model.LoginRequest;
import school.hei.exam.model.LoginResponse;
import school.hei.exam.security.CustomUserHeiDetails;
import school.hei.exam.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest request) {
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    String token = jwtService.generateToken(userDetails);
    String role = ((CustomUserHeiDetails) userDetails).user().getRole().name();

    return new LoginResponse(token, role);
  }
}
