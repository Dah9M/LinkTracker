package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class GithubClient extends BaseApiClient {

    @Value("${github.token:}")
    private String githubToken;

    private HttpEntity<Void> buildRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.set("Authorization", "token " + githubToken);
        }
        return new HttpEntity<>(headers);
    }

    private String convertUrlToApi(String url) {
        try {
            URL parsedUrl = new URL(url);
            String[] parts = parsedUrl.getPath().split("/");
            String apiUrl = String.format("https://api.github.com/repos/%s/%s", parts[1], parts[2]);
            log.debug("Преобразован URL {} в API URL {}", url, apiUrl);
            return apiUrl;
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка разбора GitHub URL: " + url, e);
        }
    }

    public Optional<Instant> fetchGithubUpdates(String url) {
        String apiUrl = convertUrlToApi(url);
        try {
            log.info("Отправка запроса на GitHub API для репозитория: {}", url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    org.springframework.http.HttpMethod.GET,
                    buildRequestEntity(),
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String updatedAt = (String) response.getBody().get("pushed_at");
                Instant instant = Instant.parse(updatedAt);
                log.debug("Получено время последнего коммита {} для репозитория {}", instant, url);
                return Optional.of(instant);
            } else {
                log.warn("GitHub API вернул статус {} для URL {}", response.getStatusCode(), url);
            }
        } catch (Exception e) {
            log.error("Ошибка при запросе к GitHub API для репозитория {}: {}", url, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Instant> fetchLatestIssueOrPrUpdate(String url) {
        String baseApiUrl = convertUrlToApi(url);
        String issuesApiUrl = baseApiUrl + "/issues?state=all&sort=created&direction=desc&per_page=1";

        try {
            log.info("Отправка запроса на получение последнего issue/PR для репозитория: {}", url);

            ResponseEntity<java.util.List> response = restTemplate.exchange(
                    issuesApiUrl,
                    org.springframework.http.HttpMethod.GET,
                    buildRequestEntity(),
                    java.util.List.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
                Map<String, Object> latestItem = (Map<String, Object>) response.getBody().get(0);
                String createdAt = (String) latestItem.get("created_at");
                Instant instant = Instant.parse(createdAt);
                log.debug("Получено время создания последнего issue/PR {} для репозитория {}", instant, url);
                return Optional.of(instant);
            } else {
                log.warn("GitHub issues API вернул неуспешный статус {} для URL {}", response.getStatusCode(), url);
            }
        } catch (Exception e) {
            log.error("Ошибка при запросе последних issue/PR для репозитория {}: {}", url, e.getMessage(), e);
        }
        return Optional.empty();
    }
}