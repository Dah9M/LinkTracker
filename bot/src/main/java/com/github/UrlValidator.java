package com.github;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {

    public static String isValidUrl(String[] message) {

        if (message.length != 2) {
            return "NO_URL";
        }

        String url = message[1].trim();

        if (url.isBlank()) {
            return "NO_VALID";
        }

        if (!(url.startsWith("http://")) && !(url.startsWith("https://"))) {
            return "NO_VALID";
        }

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return "NO_VALID";
        }

        return url;
    }
}
