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
public class StackOverflowCLient {
    private final RestTemplate restTemplate = new RestTemplate();

    private String convertUrlToApi(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Provided url is null or empty");
        }
        try {
            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();
            if (!"stackoverflow.com".equalsIgnoreCase(host) && !"www.stackoverflow.com".equalsIgnoreCase(host)) {
                throw new IllegalArgumentException("Provided url contains invalid host");
            }
            String[] parts = parsedUrl.getPath().split("/");
            if (parts.length < 3 || !"questions".equalsIgnoreCase(parts[1]) || parts[2].isEmpty()) {
                throw new IllegalArgumentException("URL must be in the format: /questions/{questionId}/...");
            }
            String questionId = parts[2];
            try {
                Long.parseLong(questionId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Provided questionId is not a number", e);
            }
            String apiUrl = String.format("https://api.stackexchange.com/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow", questionId);
            log.debug("Converted URL {} to API URL {}", url, apiUrl);
            return apiUrl;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL provided: " + url, e);
        }
    }

    public Optional<Instant> fetchStackOverflowUpdates(String url) {
        String apiUrl = convertUrlToApi(url);
        if (apiUrl == null) {
            return Optional.empty();
        }
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
                if (items != null && !items.isEmpty()) {
                    Map<String, Object> questionData = items.get(0);
                    Number lastActivityDate = (Number) questionData.get("last_activity_date");
                    if (lastActivityDate != null) {
                        Instant instant = Instant.ofEpochSecond(lastActivityDate.longValue());
                        log.debug("Fetched StackOverflow update time {} for URL {})", instant, url);
                        return Optional.of(instant);
                    } else {
                        log.warn("StackOverflow response for URL {} does not contain last_activity_date", url);
                    }
                } else {
                    log.warn("StackOverflow response for URL {} has empty items", url);
                }
            } else {
                log.warn("StackOverflow API returned non-success status {} for URL {}", response.getStatusCode(), url);
            }
        } catch (HttpClientErrorException.TooManyRequests ex) {
            log.error("Too many requests error for StackOverflow API for URL {}: {}", url, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Error fetching StackOverflow updates for URL {}: {}", url, e.getMessage(), e);
        }
        return Optional.empty();
    }

}
