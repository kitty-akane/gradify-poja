package school.hei.exam.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Exam(
    UUID id, UUID offeringId, String label, Instant examDate, BigDecimal coefficient) {}
