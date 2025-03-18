package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//TODO: согласовать чтобы по всем классам гулял чат айди только в формате String
@Data
@AllArgsConstructor
public class LinkUpdate {
    private Long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
