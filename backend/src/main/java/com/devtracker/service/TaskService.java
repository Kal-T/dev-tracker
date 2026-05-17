package com.devtracker.service;

import com.devtracker.config.TaskMapper;
import com.devtracker.dto.request.CreateTaskRequest;
import com.devtracker.dto.request.UpdateTaskRequest;
import com.devtracker.dto.response.PaginatedResponse;
import com.devtracker.dto.response.TaskResponse;
import com.devtracker.entity.Task;
import com.devtracker.entity.TaskPriority;
import com.devtracker.entity.TaskStatus;
import com.devtracker.entity.User;
import com.devtracker.exception.ResourceNotFoundException;
import com.devtracker.repository.TaskRepository;
import com.devtracker.repository.TaskSpecification;
import com.devtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public PaginatedResponse<TaskResponse> getTasksPaginated(int page, int size, String status, String search) {
        log.debug("Fetching paginated tasks - page: {}, size: {}, status: {}, search: {}", page, size, status, search);
        
        // Sort by createdAt descending so newest tasks appear first
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Task> spec = TaskSpecification.filterBy(status, search);
        
        Page<Task> taskPage = taskRepository.findAll(spec, pageable);
        
        List<TaskResponse> mappedData = taskPage.getContent().stream()
                .map(taskMapper::toResponse)
                .toList();

        return new PaginatedResponse<>(
                mappedData,
                taskPage.getTotalElements(),
                taskPage.getNumber(),
                taskPage.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        log.debug("Fetching task by id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request, User owner) {
        log.info("Creating new task titled: '{}' for owner: {}", request.title(), owner.getEmail());
        
        TaskPriority priority;
        try {
            priority = TaskPriority.valueOf(request.priority().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            priority = TaskPriority.MEDIUM; // Default priority fallback
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO) // New tasks always default to TODO
                .priority(priority)
                .tags(request.tags() != null ? new ArrayList<>(request.tags()) : new ArrayList<>())
                .owner(owner)
                .build();

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        log.info("Updating task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            try {
                task.setStatus(TaskStatus.valueOf(request.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid task status: " + request.status());
            }
        }
        if (request.priority() != null) {
            try {
                task.setPriority(TaskPriority.valueOf(request.priority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid task priority: " + request.priority());
            }
        }
        if (request.tags() != null) {
            task.getTags().clear();
            task.getTags().addAll(request.tags());
        }

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Transactional
    public TaskResponse updateTaskStatus(UUID id, String status) {
        log.info("Optimistic UI Patch - Updating status of task ID: {} to '{}'", id, status);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        try {
            task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid task status: " + status);
        }

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(UUID id) {
        log.info("Deleting task with ID: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getTaskStats() {
        log.debug("Compiling status aggregated statistics");
        List<Object[]> rawStats = taskRepository.countTasksByStatus();
        
        Map<String, Long> statsMap = new LinkedHashMap<>();
        // Initialize all statuses with 0 to avoid front-end chart rendering issues
        for (TaskStatus status : TaskStatus.values()) {
            statsMap.put(status.name(), 0L);
        }

        for (Object[] row : rawStats) {
            if (row[0] instanceof TaskStatus status) {
                statsMap.put(status.name(), (Long) row[1]);
            }
        }
        return statsMap;
    }
}
