package com.devtracker.config;

import com.devtracker.dto.response.TaskResponse;
import com.devtracker.entity.Task;
import org.springframework.stereotype.Component;

/**
 * TaskMapper manages conversion between Task entity and TaskResponse DTO.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * TRADEOFF ANALYSIS: MANUAL MAPPING vs. MAPSTRUCT
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * APPROACH 1: MANUAL MAPPING (Implemented below)
 * Pros:
 *   - Simple & transparent: Zero compilation-time setup or special compiler plugin requirements.
 *   - Debuggable: Direct step-in debugging works out of the box with any IDE.
 *   - Zero dependencies: Keeps pom.xml lightweight.
 *   - Safe: Resilient to refactoring mismatch since compilation error flags any type differences instantly.
 * Cons:
 *   - Boilerplate: Must write copy constructor logic by hand for each new field.
 *
 * APPROACH 2: MAPSTRUCT (Alternative)
 * Pros:
 *   - Automatic implementation: Generates boilerplate mapping at compile time using Java annotation processors.
 *   - Performant: Generated code is standard, fast, direct getter/setter calls (no reflection).
 *   - Built-in type conversions: Auto-handles standard transformations (e.g., Enum to String).
 * Cons:
 *   - Compiler setup: Requires configuring mapstruct & mapstruct-processor in the maven-compiler-plugin.
 *   - Mismatch errors: Mapping errors are thrown at compile time, sometimes yielding complex annotation errors.
 *
 * MapStruct Implementation Example:
 * <code>
 *   @Mapper(componentModel = "spring")
 *   public interface TaskMapper {
 *       @Mapping(source = "owner.email", target = "ownerEmail")
 *       TaskResponse toResponse(Task task);
 *   }
 * </code>
 */
@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        if (task == null) {
            return null;
        }

        String ownerEmail = null;
        if (task.getOwner() != null) {
            ownerEmail = task.getOwner().getEmail();
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().name() : null,
                task.getPriority() != null ? task.getPriority().name() : null,
                task.getTags(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                ownerEmail,
                task.getType() != null ? task.getType().name() : null
        );
    }
}
