package com.github;

import com.github.exception.UrlValidationErrorType;
import com.github.exception.UrlValidationException;

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
}
