package com.devtracker.config;

import com.devtracker.entity.*;
import com.devtracker.repository.TaskRepository;
import com.devtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the in-memory H2 database with demo data on every startup.
 * Runs only in dev profile (ddl-auto: create-drop resets the schema each restart).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Seeding database with demo data...");

        // ── Users ──────────────────────────────────────────────────────────────
        User alice = userRepository.save(User.builder()
                .email("alice@devtracker.com")
                .passwordHash(passwordEncoder.encode("alice123"))
                .displayName("Alice Johnson")
                .build());

        User bob = userRepository.save(User.builder()
                .email("bob@devtracker.com")
                .passwordHash(passwordEncoder.encode("bob123"))
                .displayName("Bob Smith")
                .build());

        // ── Alice's tasks ──────────────────────────────────────────────────────
        taskRepository.saveAll(List.of(
                Task.builder()
                        .title("Setup CI/CD pipeline")
                        .description("Configure GitHub Actions for automated build, test, and deploy.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.HIGH)
                        .tags(List.of("devops", "infrastructure"))
                        .owner(alice)
                        .build(),

                Task.builder()
                        .title("Design database schema")
                        .description("Draft ER diagram and finalize entity relationships for v1.")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(TaskPriority.HIGH)
                        .tags(List.of("backend", "database"))
                        .owner(alice)
                        .build(),

                Task.builder()
                        .title("Write unit tests for TaskService")
                        .description("Achieve 80 % coverage for the task service layer using JUnit 5.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.MEDIUM)
                        .tags(List.of("testing", "backend"))
                        .owner(alice)
                        .build(),

                Task.builder()
                        .title("Code review — auth module")
                        .description("Review PR #12 and leave detailed feedback.")
                        .status(TaskStatus.DONE)
                        .priority(TaskPriority.LOW)
                        .tags(List.of("review"))
                        .owner(alice)
                        .build()
        ));

        // ── Bob's tasks ────────────────────────────────────────────────────────
        taskRepository.saveAll(List.of(
                Task.builder()
                        .title("Fix login redirect bug")
                        .description("Users are redirected to 404 after a successful login on mobile.")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(TaskPriority.HIGH)
                        .tags(List.of("bug", "frontend"))
                        .owner(bob)
                        .build(),

                Task.builder()
                        .title("Update API documentation")
                        .description("Sync Swagger/OpenAPI spec with the latest endpoint changes.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.LOW)
                        .tags(List.of("docs"))
                        .owner(bob)
                        .build(),

                Task.builder()
                        .title("Implement full-text search")
                        .description("Add fuzzy search across task title and description fields.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.MEDIUM)
                        .tags(List.of("feature", "backend"))
                        .owner(bob)
                        .build(),

                Task.builder()
                        .title("Performance optimisation — board view")
                        .description("Reduce initial load time by lazy-loading off-screen task cards.")
                        .status(TaskStatus.DONE)
                        .priority(TaskPriority.MEDIUM)
                        .tags(List.of("performance", "frontend"))
                        .owner(bob)
                        .build()
        ));

        log.info("Seeded 2 users and 8 tasks successfully.");
    }
}
