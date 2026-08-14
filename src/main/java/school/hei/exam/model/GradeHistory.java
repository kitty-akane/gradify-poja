package school.hei.exam.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record GradeHistory(
    UUID id,
    UUID gradeId,
    BigDecimal oldValue,
    BigDecimal newValue,
    String reason,
    UUID modifiedById,
    Instant modifiedAt) {}
