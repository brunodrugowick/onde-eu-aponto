package dev.drugowick.ondeeuaponto.domain.jira;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

/**
 * Determines a Jira user given a OAuth2AuthenticationToken.
 *
 * You may provide other implementations to this, like a database query to get the user or a parse on the user email to
 * generate it.
 */
public interface JiraUser {

    String getJiraUsername(OAuth2AuthenticationToken oauthUser);
}
