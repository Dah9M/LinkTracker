package com.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListLinkResponse {
    private List<LinkResponse> links;
    private int size;
}
