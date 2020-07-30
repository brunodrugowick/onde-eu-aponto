package dev.drugowick.ondeeuaponto.domain.repository;

import dev.drugowick.ondeeuaponto.domain.model.JiraIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {

    List<JiraIssue> findAllByDescriptionContaining(String searchTerm);
}
