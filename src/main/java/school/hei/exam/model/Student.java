package school.hei.exam.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Student(UUID id, UUID userId, String studentNumber, LocalDate entryDate) {}
