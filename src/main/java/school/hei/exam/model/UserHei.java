package school.hei.exam.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserHei(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String address,
    Role role) {}
