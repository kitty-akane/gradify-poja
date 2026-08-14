package school.hei.exam.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CourseOffering(
    UUID id, UUID courseId, UUID groupId, String academicYear, List<UUID> teacherIds) {}
