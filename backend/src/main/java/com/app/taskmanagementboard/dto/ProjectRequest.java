package com.app.taskmanagementboard.dto;

import com.app.taskmanagementboard.entity.Project;
import com.app.taskmanagementboard.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ProjectRequest(
        @NotBlank @Size(max = 255)
        String name,
        String description,
        List<TaskRequest> taskRequestList
) {
}
