package com.devtracker.controller;

import com.devtracker.dto.request.CreateTaskRequest;
import com.devtracker.dto.request.UpdateTaskRequest;
import com.devtracker.dto.response.PaginatedResponse;
import com.devtracker.dto.response.TaskResponse;
import com.devtracker.entity.User;
import com.devtracker.repository.UserRepository;
import com.devtracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import com.devtracker.dto.request.BulkStatusUpdateRequest;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID cursor
    ) {
        if (cursor != null) {
            return ResponseEntity.ok(taskService.getTasksCursorPaginated(cursor, size));
        }
        return ResponseEntity.ok(taskService.getTasksPaginated(page, size, status, search));
    }

    @PostMapping("/bulk-status")
    public ResponseEntity<Integer> bulkUpdateStatus(@Valid @RequestBody BulkStatusUpdateRequest request) {
        int updatedCount = taskService.bulkUpdateStatus(request.ids(), request.status());
        return ResponseEntity.ok(updatedCount);
    }

    /**
     * NOTE: Stat route placed explicitly before GET /{id} path 
     * to prevent Spring MVC from matching "stats" as a UUID string path variable.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(taskService.getTaskStats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        // Resolve authenticated user if available, fallback to seeded Alice for testing permit-all setup
        String email = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymousUser";

        User owner = userRepository.findByEmail(email)
                .or(() -> userRepository.findByEmail("alice@devtracker.com"))
                .or(() -> userRepository.findAll().stream().findFirst())
                .orElseThrow(() -> new IllegalStateException("No default users found in database to act as task owner. Ensure DataInitializer is enabled."));

        TaskResponse response = taskService.createTask(request, owner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        if (status == null) {
            throw new IllegalArgumentException("Field 'status' must be present in PATCH body.");
        }
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
