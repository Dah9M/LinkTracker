package com.github.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubClient {
    private final RestTemplate restTemplate = new RestTemplate();


    // https://api.github.com/repos/{owner}/{repo}/issues
    // https://api.github.com/repos/{owner}/{repo}/pulls
    // https://api.github.com/repos/{owner}/{repo}/issues/comments

    //Todo: Доработать валидацию и обработку исключений
    private String convertUrlToApi(String url) {
        String parts[] = url.split("/");
        if (parts.length >= 5) {
            String owner = parts[3];
            String repo = parts[4];
            return String.format("https://api.github.com/repos/%s/%s", owner, repo);
        }

        return null;
    }

    public Optional<Instant> fetchGithubUpdates(String url) {
        String apiUrl = convertUrlToApi(url);

        if (apiUrl == null) {
            return Optional.empty();
        }

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String updatedAt = (String) response.getBody().get("updated_at");
                return Optional.of(Instant.parse(updatedAt));
            }

        } catch (HttpClientErrorException.TooManyRequests ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}