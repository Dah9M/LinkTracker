package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(exclude = "subscriptions")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false, unique = true)
    private String chatId;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt = Instant.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subscription> subscriptions = new HashSet<>();

    public AppUser(String chatId) {
        this.chatId = chatId;
    }
}
