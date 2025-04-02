package repository;

import model.SubscriptionId;
import model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    List<Subscription> findByUser_ChatId(String chatId);
    Optional<Subscription> findByUser_ChatIdAndLink_Url(String chatId, String url);
}
