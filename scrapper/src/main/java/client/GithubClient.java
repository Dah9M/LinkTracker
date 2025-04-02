package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class GithubClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private String convertUrlToApi(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Provided url is null or empty");
        }

        try {
            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();

            if (!"github.com".equalsIgnoreCase(host) && !"www.github.com".equalsIgnoreCase(host)) {
                throw new IllegalArgumentException("Provided url contains invalid host");
            }
            String[] parts = parsedUrl.getPath().split("/");
            if (parts.length < 3 || parts[1].isEmpty() || parts[2].isEmpty()) {
                throw new IllegalArgumentException("URL must contain both owner and repository name in the path.");
            }
            String owner = parts[1];
            String repo = parts[2];
            String apiUrl = String.format("https://api.github.com/repos/%s/%s", owner, repo);
            log.info("Converted URL {} to API URL {}", url, apiUrl);
            return apiUrl;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL provided: " + url, e);
        }
    }

    public Optional<Instant> fetchGithubUpdates(String url) {
        String apiUrl = convertUrlToApi(url);
        if (apiUrl == null) {
            return Optional.empty();
        }
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String updatedAt = (String) response.getBody().get("pushed_at");
                Instant instant = Instant.parse(updatedAt);
                log.debug("Fetched GitHub update time {} for URL {}", instant, url);
                return Optional.of(instant);
            } else {
                log.warn("GitHub API returned non-success status {} for URL {}", response.getStatusCode(), url);
            }
        } catch (HttpClientErrorException.TooManyRequests ex) {
            log.error("Too many requests error for GitHub API for URL {}: {}", url, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Error fetching GitHub updates for URL {}: {}", url, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Instant> fetchLatestIssueOrPrUpdate(String url) {

        String baseApiUrl = convertUrlToApi(url);
        String issuesApiUrl = baseApiUrl + "/issues?state=all&sort=created&direction=desc&per_page=1";

        try {
            ResponseEntity<List> response = restTemplate.getForEntity(issuesApiUrl, List.class);
            if (response.getStatusCode().is2xxSuccessful() &&
                    response.getBody() != null && !response.getBody().isEmpty()) {

                Map<String, Object> latestItem = (Map<String, Object>) response.getBody().get(0);
                String createdAt = (String) latestItem.get("created_at");
                Instant instant = Instant.parse(createdAt);
                log.debug("Fetched latest issue/PR creation time {} for URL {}", instant, url);
                return Optional.of(instant);
            } else {
                log.warn("GitHub issues API returned non-success status {} for URL {}",
                        response.getStatusCode(), url);
            }
        } catch (Exception e) {
            log.error("Error fetching latest issue/PR updates for URL {}: {}", url, e.getMessage(), e);
        }
        return Optional.empty();
    }

}
