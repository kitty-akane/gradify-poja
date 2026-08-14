package school.hei.exam.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record Enrollment(
    UUID id, UUID studentId, UUID groupId, String academicYear, Level level, Track track) {}
