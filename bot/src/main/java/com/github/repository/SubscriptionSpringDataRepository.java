package com.github.repository;

import com.github.entity.SubscriptionEntity;
import com.github.entity.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionSpringDataRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {

}
