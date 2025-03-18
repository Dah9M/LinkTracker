package com.github.repository;

import com.github.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkSpringDataRepository extends JpaRepository<LinkEntity, Long> {
    LinkEntity findByUrl(String url);
}
