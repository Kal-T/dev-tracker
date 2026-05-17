package com.devtracker.repository;

import com.devtracker.entity.Task;
import com.devtracker.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TaskSpecification {

    public static Specification<Task> filterBy(String statusStr, String search) {
        return Specification.where(hasStatus(statusStr))
                .and(hasSearchQuery(search));
    }

    private static Specification<Task> hasStatus(String statusStr) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(statusStr)) {
                return null;
            }
            try {
                TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
                return cb.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                return null; // Invalid status passed, ignore filter
            }
        };
    }

    private static Specification<Task> hasSearchQuery(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return null;
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
            );
        };
    }
}
