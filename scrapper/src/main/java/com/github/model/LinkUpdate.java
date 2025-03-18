package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LinkUpdate {
    private Long id;
    private String url;
    private String description;
    private List<String> tgChatIds;
}
