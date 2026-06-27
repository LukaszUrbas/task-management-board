package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record SubprojectResponse(
        Long id,
        String name,
        String description,
        TaskStatus status,
        LocalDate deadline,
        Long projectId,
        List<TaskResponse> taskResponseList,
        Instant createdAt,
        Instant updatedAt
) {
}
