package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class StackOverflowCLient extends BaseApiClient {

    private String convertUrlToApi(String url) {
        try {
            URL parsedUrl = new URL(url);
            String[] parts = parsedUrl.getPath().split("/");
            String questionId = parts[2];
            String apiUrl = String.format(
                    "https://api.stackexchange.com/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow",
                    questionId
            );
            log.debug("Преобразован URL {} в API URL {}", url, apiUrl);
            return apiUrl;
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка разбора StackOverflow URL: " + url, e);
        }
    }

    public Optional<Instant> fetchStackOverflowUpdates(String url) {
        String apiUrl = convertUrlToApi(url);
        return fetchApiResponse(apiUrl, url)
                .flatMap(response -> {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) Objects.requireNonNull(response.getBody()).get("items");
                    if (items != null && !items.isEmpty()) {
                        Number lastActivityDate = (Number) items.getFirst().get("last_activity_date");
                        Instant instant = Instant.ofEpochSecond(lastActivityDate.longValue());
                        log.debug("Получено время последней активности {} для вопроса {}", instant, url);
                        return Optional.of(instant);
                    } else {
                        log.warn("Ответ StackOverflow API содержит пустой список items для вопроса {}", url);
                        return Optional.empty();
                    }
                });
    }
}