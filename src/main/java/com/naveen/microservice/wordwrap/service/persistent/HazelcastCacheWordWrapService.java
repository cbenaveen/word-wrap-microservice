package com.naveen.microservice.wordwrap.service.persistent;

import com.hazelcast.core.HazelcastInstance;
import com.naveen.microservice.wordwrap.model.WrappedContent;
import com.naveen.microservice.wordwrap.repository.CachedContentHazelcastContentRepository;
import com.naveen.microservice.wordwrap.service.Utils;
import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.model.CachedContent;
import com.naveen.microservice.wordwrap.model.Content;
import com.naveen.microservice.wordwrap.wrap.WrapperTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class HazelcastCacheWordWrapService extends AbstractPersistentWordWrapService {
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
        final Content c = Utils.getContent(content);
        long id = hazelcastInstance.getFlakeIdGenerator(ID_GENERATOR_NAME).newId();
        CachedContent build = CachedContent.builder().id(id).content(c).build();
        cachedContentHazelcastContentRepository.save(build);

        return build;
    }

    @Override
    public WrappedContent wrap(long contentId, int maxLength, int itemsPerPage) {
        Optional<CachedContent> cachedContentById = cachedContentHazelcastContentRepository.findById(contentId);

        if (cachedContentById.isEmpty()) {
            throw new IllegalArgumentException("No Content maps to the content id " + contentId);
        }

        CachedContent cachedContent = cachedContentById.get();
        Content content = cachedContent.getContent();

        AbstractContentWrapIterator charByCharWrapper = Utils.getContentWrapperIterator(applicationContext,
                WrapperTypes.CHAR_POSITION_BASED_CONTENT_WRAPPER, content, maxLength);

        List<String> lines = new ArrayList();

        while(itemsPerPage > 0 && charByCharWrapper.hasNext()) {
            lines.add(charByCharWrapper.next());
            itemsPerPage -= 1;
        }

        return  WrappedContent.builder().currentPosition(charByCharWrapper.currentPosition())
                .lines(lines).build();
    }

    @Override
    public WrappedContent wrap(long contentId, int maxLength) {
        return wrap(contentId, maxLength, defaultItemsPerPage);
    }

    @Override
    public WrappedContent wrap(long contentId) {
        return wrap(contentId, defaultMaxLength, defaultItemsPerPage);
    }
}
