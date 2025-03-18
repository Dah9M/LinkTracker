package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatUser {
    private Long id;
    private String chatId;
    private LocalDateTime registered_at;
}
