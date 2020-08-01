package dev.drugowick.ondeeuaponto.domain.service.client.jira;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class JiraIssuesModel {

    private List<JiraIssueModel> issues;
}
