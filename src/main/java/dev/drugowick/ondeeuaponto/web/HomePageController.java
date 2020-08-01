package dev.drugowick.ondeeuaponto.web;

import dev.drugowick.ondeeuaponto.domain.service.JiraService;
import dev.drugowick.ondeeuaponto.domain.service.client.jira.JiraIssueModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomePageController {

    private final JiraService jiraService;

    @ModelAttribute("username") public String username(OAuth2AuthenticationToken user) { return jiraService.getJiraUsername(user); }
    @ModelAttribute("jiraBaseUrl") public String jiraBaseUrl() { return jiraService.jiraBaseUrl(); }
    @ModelAttribute("maxResults") public int maxResults() { return jiraService.maxResults(); }

    @RequestMapping({ "/", "/search" })
    public String search(@RequestParam(name = "query", required = false, defaultValue = "") String query,
                         @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                         Model model, OAuth2AuthenticationToken user) {
        log.info("User {} started search for {} page {}", user, query, page);

        final List<JiraIssueModel> issues = jiraService.getAssignedIssuesWithTermsOrDefaultQuery(user, query, page);

        model.addAttribute("issues", issues);
        model.addAttribute("query", query);
        model.addAttribute("page", page);

        return "index";
    }

}
