package com.naveen.microservice.wordwrap.repository;

import com.naveen.microservice.wordwrap.model.CachedContent;
import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CachedContentHazelcastContentRepository extends HazelcastRepository<CachedContent, Long> {
}
