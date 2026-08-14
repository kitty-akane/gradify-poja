package school.hei.exam.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record Teacher(UUID id, UUID userId) {}
