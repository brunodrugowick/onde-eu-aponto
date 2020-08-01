package dev.drugowick.ondeeuaponto.domain.service.client.jira;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jira", url = "${jira.baseUrl}", configuration = JiraClient.Configuration.class)
public interface JiraClient {

    @GetMapping("/api/2/search")
    JiraIssuesModel executeJql(@RequestParam("jql") String jql, @RequestParam("maxResults") int maxResults);
    
    public static class Configuration {

        @Value("${jira.integrationUsername}") String username;
        @Value("${jira.integrationPassword}") String password;

        @Bean public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
            return new BasicAuthRequestInterceptor(username, password);
        }
    }

}
