package dev.drugowick.ondeeuaponto.domain.service.client.jira;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JiraIssueModel {

    private String key;
    private Field fields;
}
