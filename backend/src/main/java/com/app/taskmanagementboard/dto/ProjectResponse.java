package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.Project;
import com.app.taskmanagementboard.entity.TaskStatus;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectResponse projectResponse,
        List<TaskResponse> taskResponseList,
        Instant createdAt,
        Instant updatedAt
) {
}
