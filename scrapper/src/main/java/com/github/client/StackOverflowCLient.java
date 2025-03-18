package com.github.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class StackOverflowCLient {
    private final RestTemplate restTemplate = new RestTemplate();

    //Todo: Доработать валидацию и обработку исключений
    private String convertUrlToApi(String url) {
        try {
            URL parsedUrl = new URL(url);

            String[] parts = parsedUrl.getPath().split("/");
            if (parts.length >= 3 && "questions".equals(parts[1])) {
                String questionId = parts[2];

                return "https://api.stackexchange.com/2.3/questions/"
                        + questionId
                        + "?order=desc&sort=activity&site=stackoverflow";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
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
                    Integer lastActivityDate = (Integer) questionData.get("last_activity_date");

                    if (lastActivityDate != null) {
                        return Optional.of(Instant.ofEpochSecond(lastActivityDate));
                    }
                }
            }
        } catch (HttpClientErrorException.TooManyRequests ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
