package com.app.taskmanagementboard.service;

import com.app.taskmanagementboard.dto.TaskRequest;
import com.app.taskmanagementboard.dto.TaskResponse;
import com.app.taskmanagementboard.entity.Task;
import com.app.taskmanagementboard.entity.TaskStatus;
import com.app.taskmanagementboard.exception.TaskNotFoundException;
import com.app.taskmanagementboard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> findByStatus(TaskStatus status) {
        return taskRepository.findAllByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse findById(Long id) {
        return taskRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        Task task = Task.builder()
                .name(request.name())
                .description(request.description())
                .status(request.status())
                .deadline(request.deadline())
                .build();
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setName(request.name());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    // ── mapper ────────────────────────────────────────────────────────────────

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .subprojectId(task.getSubproject() != null ? task.getSubproject().getId() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}

