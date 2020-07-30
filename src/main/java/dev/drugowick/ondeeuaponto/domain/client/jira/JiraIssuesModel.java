package dev.drugowick.ondeeuaponto.domain.client.jira;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class JiraIssuesModel {

    private List<JiraIssueModel> issues;
}
