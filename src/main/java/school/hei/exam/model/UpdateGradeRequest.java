package school.hei.exam.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateGradeRequest(
    @NotNull @DecimalMin("0") @DecimalMax("20") BigDecimal newValue,
    @NotBlank(message = "Le motif de la modification est obligatoire") String reason) {}
