package com.naveen.microservice.wordwrap.service.inmemory;

import com.naveen.microservice.wordwrap.service.WordWrapService;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.Collection;

abstract class AbstractWordWrapService implements WordWrapService {
    protected final ApplicationContext applicationContext;
    protected final int defaultMaxLength;

    public AbstractWordWrapService(ApplicationContext applicationContext, int defaultMaxLength) {
        this.applicationContext = applicationContext;
        this.defaultMaxLength = defaultMaxLength;
    }

    @Override
    public Collection<String> wrap(String content) {
        return wrap(content, defaultMaxLength);
    }

    @Override
    public Flux<String> reactive(String content) {
        return reactive(content, defaultMaxLength);
    }
}
