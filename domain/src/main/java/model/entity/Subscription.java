package model.entity;

import model.SubscriptionId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @MapsId("linkId")
    @ManyToOne
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public Subscription(AppUser user, Link link) {
        this.user = user;
        this.link = link;
        this.id = new SubscriptionId(user.getId(), link.getId());
    }

}
