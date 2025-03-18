package com.github.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SubscriptionId {
    private Long userId;
    private Long linkId;

    public SubscriptionId() {}

    public SubscriptionId(Long userId, Long linkId) {
        this.userId = userId;
        this.linkId = linkId;
    }
}
