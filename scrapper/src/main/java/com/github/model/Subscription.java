package com.github.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Subscription {
    private String chatId;
    private String url;
    private ResourceType type;
    private Instant lastUpdated;
}
