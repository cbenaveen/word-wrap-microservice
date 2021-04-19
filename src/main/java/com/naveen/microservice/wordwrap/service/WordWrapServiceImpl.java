package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
class WordWrapServiceImpl implements WordWrapService {
    private final ApplicationContext applicationContext;
    private final int defaultMaxLength;

    WordWrapServiceImpl(ApplicationContext applicationContext,
                        @Value("${default.max.length:15}") int defaultMaxLength) {
        this.applicationContext = applicationContext;
        this.defaultMaxLength = defaultMaxLength;
    }

    @Override
    public Collection<String> wrap(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapper(content, maxLength);

        List<String> lines = new ArrayList();

        for (String line: inMemoryContentWrap) {
            lines.add(line);
        }

        return lines;
    }

    @Override
    public Collection<String> wrap(String charBuffer) {
        return wrap(charBuffer, defaultMaxLength);
    }

    @Override
    public Flux<String> reactive(String content) {
        return reactive(content, defaultMaxLength);
    }

    @Override
    public Flux<String> reactive(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapper(content, maxLength);
        return Flux.fromIterable(inMemoryContentWrap);
    }

    private AbstractContentWrapIterator getContentWrapper(String content, int maxLength) {
        return (AbstractContentWrapIterator)
                applicationContext.getBean("inMemoryContentWrapper", content, maxLength);
    }
}
