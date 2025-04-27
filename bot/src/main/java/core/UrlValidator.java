package core;

import exception.UrlValidationErrorType;
import exception.UrlValidationException;
import lombok.extern.slf4j.Slf4j;
import model.ResourceType;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class UrlValidator {

    public static String getUrlOrThrow(String[] message) {
        log.info("Начата проверка URL в сообщении: {}", (Object) message);
        if (message.length != 2) {
            log.warn("URL отсутствует в сообщении: {}", (Object) message);
            throw new UrlValidationException(UrlValidationErrorType.NO_URL, "Аргумент URL отсутствует");
        }

        String url = message[1].trim();
        if (url.isBlank()) {
            log.warn("Получен пустой URL.");
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "Пустая строка – это не URL");
        }

        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            log.warn("Неверный формат URL: {}", url);
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "URL должен начинаться с http:// или https://");
        }

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            log.error("Ошибка разбора URL: {}", url, e);
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "URL некорректен: " + e.getMessage());
        }

        log.info("URL успешно прошёл проверку: {}", url);
        return url;
    }

    public static ResourceType detectSource(String url) {
        log.info("Определение источника по URL: {}", url);

        if (url.contains("github")) {
            return ResourceType.GITHUB;
        } else if (url.contains("stackoverflow")) {
            return ResourceType.STACKOVERFLOW;
        } else {
            log.warn("Не удалось определить источник для URL: {}", url);
            throw new IllegalArgumentException("Not valid URL: " + url);
        }
    }
}
