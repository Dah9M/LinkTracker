package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionNotification {
    private String title;
    private Instant lastActivityDate;
    private List<String> users;
}

