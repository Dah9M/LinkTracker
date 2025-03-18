package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LinkResponse {
    private Long id;
    private String url;
    private List<String> tags;
    private List<String> filters;
}
