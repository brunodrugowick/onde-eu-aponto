package dev.drugowick.ondeeuaponto.web;

import dev.drugowick.ondeeuaponto.domain.client.jira.JiraClient;
import dev.drugowick.ondeeuaponto.domain.client.jira.JiraIssueModel;
import dev.drugowick.ondeeuaponto.domain.client.jira.JiraIssuesModel;
import dev.drugowick.ondeeuaponto.domain.model.JiraIssue;
import dev.drugowick.ondeeuaponto.domain.repository.JiraIssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomePageController {

    private final JiraIssueRepository jiraIssueRepository;
    private final JiraClient jiraClient;

    // TODO create a properties processor class
    @Value("${jira.maxResults:15}")
    Integer jiraMaxResults;

    @Value("${jira.username}")
    String jiraUsername;

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("issues", jiraIssueRepository.findAll());
        return "index";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        if (query.isBlank()) return index(model);

        log.info("User '" + jiraUsername + "' starting search for '" + query + "'; max results from Jira: " + jiraMaxResults);

        // TODO Create a meaningful service layer and move lots of stuff from here to there
        List<JiraIssue> allByDescriptionContaining = jiraIssueRepository.findAllByDescriptionContaining(query);

        final JiraIssuesModel jiraIssuesModel = jiraClient.assignedTo(
                "assignee = " + jiraUsername +  " and (description ~ \"" + query + "\" OR summary ~ \"" + query + "\")" +
                "and statusCategory not in (Done)", jiraMaxResults);

        jiraIssuesModel.getIssues().forEach(jiraIssueModel -> {
            JiraIssue converted = new JiraIssue();
            converted.setDescription(jiraIssueModel.getFields().getDescription());
            converted.setSummary(jiraIssueModel.getFields().getSummary());
            converted.setKey(jiraIssueModel.getKey());

            allByDescriptionContaining.add(converted);
        });

        for (JiraIssue jiraIssue : allByDescriptionContaining) {
            if (null != jiraIssue.getDescription()) {
                jiraIssue.setDescription(
                        jiraIssue.getDescription()
                                .replaceAll(
                                        "(?i)" + Pattern.quote(query),
                                        "<mark><b>" + query + "</b></mark>"));
            }

            if (null != jiraIssue.getSummary()) {
                jiraIssue.setSummary(
                        jiraIssue.getSummary()
                                .replaceAll(
                                        "(?i)" + Pattern.quote(query),
                                        "<mark><b>" + query + "</b></mark>"));
            }
        }

        model.addAttribute("issues", allByDescriptionContaining);
        model.addAttribute("query", query);

        return "index";
    }
}
