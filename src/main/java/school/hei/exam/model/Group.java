package school.hei.exam.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record Group(UUID id, String ref, Level level, String academicYear) {}
