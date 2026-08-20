package school.hei.exam.endpoint.rest.model;

import java.math.BigDecimal;
import java.util.UUID;

public record UpsertGradeDto(BigDecimal value, String reason, UUID modifiedById) {}
