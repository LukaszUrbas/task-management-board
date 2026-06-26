package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank @Size(max = 255)
        String title,

        String description,

        @NotNull
        TaskStatus status
) {}

