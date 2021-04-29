package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.wrap.model.Content;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("wordWrapServiceImpl")
class WordWrapServiceImpl extends AbstractWordWrapService {
    public static final String IN_MEMORY_CONTENT_WRAPPER = "inMemoryContentWrapper";

    @Autowired
    WordWrapServiceImpl(ApplicationContext applicationContext,
                        @Value("${default.max.length:15}") int defaultMaxLength) {
        super(applicationContext, defaultMaxLength);
    }

    @Override
    @Timed(value = "content_wrap_time")
    public Collection<String> wrap(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapperIterator(IN_MEMORY_CONTENT_WRAPPER,
                getContent(content), maxLength);

        List<String> lines = new ArrayList();

        for (String line: inMemoryContentWrap) {
            lines.add(line);
        }

        return lines;
    }

    @Override
    public Flux<String> reactive(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapperIterator(IN_MEMORY_CONTENT_WRAPPER,
                getContent(content), maxLength);
        return Flux.fromIterable(inMemoryContentWrap);
    }
}
