package school.hei.exam.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.hei.exam.repository.UserHeiRepository;

@Service
@AllArgsConstructor
public class UserHeiDetailsService implements UserDetailsService {
  private final UserHeiRepository userHeiRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userHeiRepository
        .findByEmail(email)
        .map(CustomUserHeiDetails::new)
        .orElseThrow(
            () -> new UsernameNotFoundException("Aucun utilisateur pour l'email : " + email));
  }
}
