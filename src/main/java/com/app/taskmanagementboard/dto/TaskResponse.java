package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.TaskStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt,
        Instant updatedAt
) {}

