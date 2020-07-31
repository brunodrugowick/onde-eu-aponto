package dev.drugowick.ondeeuaponto.web;

import dev.drugowick.ondeeuaponto.domain.client.jira.JiraClient;
import dev.drugowick.ondeeuaponto.domain.client.jira.JiraIssuesModel;
import dev.drugowick.ondeeuaponto.domain.model.JiraIssue;
import dev.drugowick.ondeeuaponto.domain.repository.JiraIssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
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

    @Value("${jira.baseUrl}")
    String jiraBaseUrl;

    @ModelAttribute("jiraBaseUrl")
    public String jiraBaseUrl() {
        return jiraBaseUrl;
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        if (null != principal) return principal.getName();
        return null;
    }

    @RequestMapping
    public String index(Model model, Principal principal) {
        // TODO remove this and properly configure OAuth for the domain
        if (notIcaro(principal)) return "redirect:/logout";

        model.addAttribute("issues", jiraIssueRepository.findAll());
        return "index";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("query") String query, Model model, Principal principal) {
        // TODO remove this and properly configure OAuth for the domain
        if (notIcaro(principal)) return "redirect:/logout";

        if (query.isBlank()) return index(model, principal);

        String twikiName = getTwikiName(principal);

        log.info("User '" + twikiName + "' starting search for '" + query + "'; max results from Jira: " + jiraMaxResults);

        // TODO Create a meaningful service layer and move lots of stuff from here to there
        List<JiraIssue> allByDescriptionContaining = jiraIssueRepository.findAllByDescriptionContaining(query);

        final JiraIssuesModel jiraIssuesModel = jiraClient.assignedTo(
                "assignee = " + twikiName +  " and (description ~ \"" + query + "\" OR summary ~ \"" + query + "\")" +
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

    private boolean notIcaro(Principal principal) {
        OAuth2AuthenticationToken user = (OAuth2AuthenticationToken) principal;
        String domain = user.getPrincipal().getAttributes().get("email").toString().split("@")[1];

        if (domain.equals("icaro.com.br")) return false;

        return true;
    }

    // TODO Proper user administration and information gathering to the database. Not in the controller.
    private String getTwikiName(Principal principal) {
        OAuth2AuthenticationToken user = (OAuth2AuthenticationToken) principal;
        String twikiName = user.getPrincipal().getAttributes().get("email").toString().split("@")[0];
        twikiName = twikiName.substring(0,1).toUpperCase() + twikiName.substring(1);
        final int indexOfDot = twikiName.indexOf(".");
        twikiName = twikiName.substring(0, indexOfDot)
                + twikiName.substring(indexOfDot + 1, indexOfDot + 2).toUpperCase()
                + twikiName.substring(indexOfDot + 2);
        twikiName = twikiName.replace(".", "");
        return twikiName;
    }
}
