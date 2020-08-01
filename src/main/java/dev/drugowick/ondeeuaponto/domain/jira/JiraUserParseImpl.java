package dev.drugowick.ondeeuaponto.domain.jira;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * This strategy generates a CamelCase string from the username portion of the user email. For example, for the user
 * email john.atlas@your-domain.com it generates JohnAtlas.
 */
@Component
@ConditionalOnProperty(
        value = "app.jiraUserStrategy",
        havingValue = "parse",
        matchIfMissing = true)
public class JiraUserParseImpl implements JiraUser {

    @Override
    public String getJiraUsername(OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        String twikiName = oAuth2AuthenticationToken.getPrincipal()
                //john.atlas@your-domain.com
                .getAttributes().get("email").toString()
                //john.atlas
                .split("@")[0];

        //John.atlas
        twikiName = twikiName.substring(0,1).toUpperCase() + twikiName.substring(1);

        final int indexOfDot = twikiName.indexOf(".");
        //John.
        twikiName = twikiName.substring(0, indexOfDot)
                //A
                + twikiName.substring(indexOfDot + 1, indexOfDot + 2).toUpperCase()
                //tlas
                + twikiName.substring(indexOfDot + 2);

        //JohnAtlas
        twikiName = twikiName.replace(".", "");

        return twikiName;
    }
}
