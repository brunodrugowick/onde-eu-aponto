package dev.drugowick.ondeeuaponto.domain.service;

import dev.drugowick.ondeeuaponto.domain.jira.JiraUser;
import dev.drugowick.ondeeuaponto.domain.service.client.jira.JiraClient;
import dev.drugowick.ondeeuaponto.domain.service.client.jira.JiraIssueModel;
import dev.drugowick.ondeeuaponto.domain.service.client.jira.JiraIssuesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraUser jiraUser;
    private final JiraClient jiraClient;

    @Value("${jira.maxResults:15}") private int jiraMaxResults;
    @Value("${jira.baseUrl}") private String jiraBaseUrl;
    @Value("${jira.defaultQuery:'assignee = :twikiName and statusCategory not in (Done)'}") private String jiraDefaultQuery;

    public List<JiraIssueModel> getAssignedIssuesWithTermsOrDefaultQuery(OAuth2AuthenticationToken user, String query, int page) {

        int maxResults = jiraMaxResults * page;
        String twikiName = jiraUser.getJiraUsername(user);
        String jql = "";

        if (query.equals("")) jql = jiraDefaultQuery;
        else jql = "assignee = :twikiName and (description ~ \"" + query + "\" OR summary ~ \"" + query + "\")" +
                "and statusCategory not in (Done)";
        // TODO robust variable replace
        if (jql.contains(":")) jql = jql.replaceAll(":\\w+", twikiName);

        JiraIssuesModel jiraIssuesModel = jiraClient.executeJql(jql, maxResults);

        return markMatchedWords(query, jiraIssuesModel).getIssues();
    }

    public String getJiraUsername(OAuth2AuthenticationToken user) {
        return jiraUser.getJiraUsername(user);
    }

    private JiraIssuesModel markMatchedWords(String query, JiraIssuesModel jiraIssuesModel) {
        if (query.equals("")) return jiraIssuesModel;
        for (JiraIssueModel jiraIssueModel : jiraIssuesModel.getIssues()) {
            if (null != jiraIssueModel.getFields().getDescription()) {
                jiraIssueModel.getFields().setDescription(
                        jiraIssueModel.getFields().getDescription()
                                .replaceAll(
                                        "(?i)" + Pattern.quote(query),
                                        "<mark><b>" + query.toUpperCase() + "</b></mark>"));
            }

            if (null != jiraIssueModel.getFields().getSummary()) {
                jiraIssueModel.getFields().setSummary(
                        jiraIssueModel.getFields().getSummary()
                                .replaceAll(
                                        "(?i)" + Pattern.quote(query),
                                        "<mark><b>" + query + "</b></mark>"));
            }
        }

        return jiraIssuesModel;
    }

    public String jiraBaseUrl() {
        return jiraBaseUrl.replace("/rest", "");
    }

    public int maxResults() {
        return jiraMaxResults;
    }
}
