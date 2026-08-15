package school.hei.exam.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Grade(UUID id, UUID examId, UUID studentId, BigDecimal value) {}
