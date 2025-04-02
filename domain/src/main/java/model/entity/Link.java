package model.entity;

import lombok.EqualsAndHashCode;
import model.ResourceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "subscriptions")
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(name = "last_checked_at")
    private Instant lastCheckedAt;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subscription> subscriptions = new HashSet<>();

    public Link(String url, ResourceType resourceType) {
        this.url = url;
        this.resourceType = resourceType;
    }
}