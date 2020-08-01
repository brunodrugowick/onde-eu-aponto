package dev.drugowick.ondeeuaponto.domain.jira;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
        value = "app.jiraUserStrategy",
        havingValue = "db",
        matchIfMissing = false)
public class JiraUserDBImpl implements JiraUser {

    @Override
    public String getJiraUsername(OAuth2AuthenticationToken oauthUser) {
        log.error("ERROR: using a non-implemented feature. If you don't understand what's going on, " +
                "remove the value 'db' from 'app.jiraUserStrategy'");
        return null;
    }
}
