package com.github.repository;

import com.github.entity.LinkEntity;
import com.github.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LinkRepository {

    private final LinkSpringDataRepository repository;

    @Autowired
    public LinkRepository(LinkSpringDataRepository jpaRepo) {
        this.repository = jpaRepo;
    }

    public Link create(String url, String resourceType) {
        LinkEntity entity = new LinkEntity();
        entity.setUrl(url);
        entity.setResourceType(resourceType);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        LinkEntity saved = repository.save(entity);
        return toModel(saved);
    }

    public Link findById(Long id) {
        return repository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    public Link findByUrl(String url) {
        LinkEntity entity = repository.findByUrl(url);
        return entity == null ? null : toModel(entity);
    }

    public List<Link> findAll() {
        return repository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public Link update(Link link) {
        LinkEntity entity = repository.findById(link.getId()).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setResourceType(link.getResourceType());
        entity.setLastCheckedAt(link.getLastCheckedAt());
        entity.setUpdatedAt(LocalDateTime.now());

        LinkEntity saved = repository.save(entity);
        return toModel(saved);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Link toModel(LinkEntity entity) {
        return new Link(
                entity.getId(),
                entity.getUrl(),
                entity.getResourceType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastCheckedAt()
        );
    }
}
