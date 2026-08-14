package school.hei.exam.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record Course(UUID id, String ref, String title, int credits) {}
