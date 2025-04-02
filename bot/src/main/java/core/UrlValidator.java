package core;

import exception.UrlValidationErrorType;
import exception.UrlValidationException;
import model.ResourceType;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {

    public static String getUrlOrThrow(String[] message) {
        if (message.length != 2) {
            throw new UrlValidationException(UrlValidationErrorType.NO_URL, "Аргумент URL отсутствует");
        }

        String url = message[1].trim();
        if (url.isBlank()) {
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "Пустая строка – это не URL");
        }

        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "URL должен начинаться с http:// или https://");
        }

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new UrlValidationException(UrlValidationErrorType.INVALID_URL, "URL некорректен: " + e.getMessage());
        }

        return url;
    }

    public static ResourceType detectSource(String url) {
        if (url.contains("github")) {
            return ResourceType.GITHUB;
        } else if (url.contains("stackoverflow")) {
            return ResourceType.STACKOVERFLOW;
        } else {
            throw new IllegalArgumentException("Not valid URL: " + url);
        }
    }
}
