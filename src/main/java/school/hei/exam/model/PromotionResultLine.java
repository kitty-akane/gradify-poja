package school.hei.exam.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PromotionResultLine(
    int rank,
    UUID studentId,
    String studentNumber,
    String firstName,
    String lastName,
    Track track,
    BigDecimal generalAverage,
    boolean validated) {}
