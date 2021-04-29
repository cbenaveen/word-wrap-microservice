package com.naveen.microservice.wordwrap.service;

import com.hazelcast.core.HazelcastInstance;
import com.naveen.microservice.wordwrap.repository.CachedContentHazelcastContentRepository;
import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.wrap.model.CachedContent;
import com.naveen.microservice.wordwrap.wrap.model.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("hazelcastCacheWordWrapService")
class HazelcastCacheWordWrapService extends AbstractPersistentWordWrapService {
    private static final String HAZELCAST_CONTENT_WRAPPER = "hazelcastContentWrapper";
    private static final String ID_GENERATOR_NAME = "cached-content-flake-id-gen";

    private final CachedContentHazelcastContentRepository cachedContentHazelcastContentRepository;
    private final HazelcastInstance hazelcastInstance;

    @Autowired
    HazelcastCacheWordWrapService(final ApplicationContext applicationContext,
                                  @Value("${default.max.length:15}") int defaultMaxLength,
                                  @Value("${default.items.per.page:3}") int defaultItemsPerPage,
                                  final CachedContentHazelcastContentRepository cachedContentHazelcastContentRepository,
                                  final HazelcastInstance hazelcastInstance) {
        super(applicationContext, defaultMaxLength, defaultItemsPerPage);
        this.cachedContentHazelcastContentRepository = cachedContentHazelcastContentRepository;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public CachedContent create(String content) {
        Content c = getContent(content);

        long id = hazelcastInstance.getFlakeIdGenerator(ID_GENERATOR_NAME).newId();
        log.info("Generated ID for the new content is {}", id);
        return cachedContentHazelcastContentRepository.save(CachedContent.builder().id(id).content(c).build());
    }

    @Override
    public Collection<String> wrap(long contentId, int maxLength, int itemsPerPage) {
        AbstractContentWrapIterator contentWrapperIterator = getContentWrapperIterator(HAZELCAST_CONTENT_WRAPPER,
                cachedContentHazelcastContentRepository.findById(contentId).get(), maxLength);

        List<String> lines = new ArrayList();

        while(itemsPerPage > 0 && contentWrapperIterator.hasNext()) {
            lines.add(contentWrapperIterator.next());
            itemsPerPage -= 1;
        }

        return lines;
    }

    @Override
    public Collection<String> wrap(long contentId, int maxLength) {
        return wrap(contentId, maxLength, defaultItemsPerPage);
    }

    @Override
    public Collection<String> wrap(long contentId) {
        return wrap(contentId, defaultMaxLength, defaultItemsPerPage);
    }
}
