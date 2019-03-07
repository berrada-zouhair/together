package com.together.utils;

import java.net.URI;

public class UrlUtils {

    private UrlUtils() {
    }

    public static String extractIdFromUri(URI uri) {
        String uriString = uri.toString();
        String[] split = uriString.split("/");
        return split[split.length - 1];
    }
}
