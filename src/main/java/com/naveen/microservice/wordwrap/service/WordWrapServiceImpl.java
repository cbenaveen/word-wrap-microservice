package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.wrap.model.Content;
import io.micrometer.core.annotation.Timed;
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
    @Timed(value = "content_wrap_time")
    public Collection<String> wrap(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapper(getContent(content), maxLength);

        List<String> lines = new ArrayList();

        for (String line: inMemoryContentWrap) {
            lines.add(line);
        }

        return lines;
    }

    private Content getContent(final String content) {
        return Content.builder().content(content).build();
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
        AbstractContentWrapIterator inMemoryContentWrap = getContentWrapper(getContent(content), maxLength);
        return Flux.fromIterable(inMemoryContentWrap);
    }

    private AbstractContentWrapIterator getContentWrapper(Content content, int maxLength) {
        return (AbstractContentWrapIterator)
                applicationContext.getBean("inMemoryContentWrapper", content, maxLength);
    }
}
