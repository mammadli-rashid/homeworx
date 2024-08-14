package com.safitech.homeworx.repository;

import com.safitech.homeworx.entity.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaRepository extends MongoRepository<Media, String> {
    Optional<Media> findByMediaIdAndActiveStatus(long id, int activeStatus);
}
