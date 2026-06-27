package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record SubprojectRequest(
        @NotBlank @Size(max = 255)
        String name,
        String description,
        @NotNull
        TaskStatus status,
        LocalDate deadline,
        @NotNull
        Long projectId
) {
}
