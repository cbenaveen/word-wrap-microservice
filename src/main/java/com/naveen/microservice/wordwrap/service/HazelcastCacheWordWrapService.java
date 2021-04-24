package com.naveen.microservice.wordwrap.service;

import com.hazelcast.core.HazelcastInstance;
import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.wrap.model.CachedContent;
import com.naveen.microservice.wordwrap.wrap.model.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
class HazelcastCacheWordWrapService extends AbstractPersistentWordWrapService {
    public static final String HAZELCAST_CONTENT_WRAPPER = "hazelcastContentWrapper";

    private static final String ID_GENERATOR_NAME = "cached-content-flake-id-gen";
    private static final String CACHED_CONTENT_MAP_NAME = "cached-contents";

    private final HazelcastInstance hazelcastInstance;

    @Autowired
    HazelcastCacheWordWrapService(ApplicationContext applicationContext,
                                  @Value("${default.max.length:15}")  int defaultMaxLength,
                                  @Value("${default.items.per.page:3}")  int defaultItemsPerPage,
                                  final HazelcastInstance hazelcastInstance) {
        super(applicationContext, defaultMaxLength, defaultItemsPerPage);
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public CachedContent create(String content) {
        Content c = getContent(content);
        long id = hazelcastInstance.getFlakeIdGenerator(ID_GENERATOR_NAME).newId();

        CachedContent cachedContent = CachedContent.builder().id(id).content(c).build();
        hazelcastInstance.getMap(CACHED_CONTENT_MAP_NAME).putIfAbsent(id, cachedContent);

        return cachedContent;
    }

    @Override
    public CachedContent get(long contentId) {
        return (CachedContent) hazelcastInstance.getMap(CACHED_CONTENT_MAP_NAME).get(contentId);
    }

    @Override
    public Collection<String> wrap(long contentId, int maxLength, int itemsPerPage) {
        AbstractContentWrapIterator contentWrapperIterator = getContentWrapperIterator(HAZELCAST_CONTENT_WRAPPER,
                this, contentId, maxLength);

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
