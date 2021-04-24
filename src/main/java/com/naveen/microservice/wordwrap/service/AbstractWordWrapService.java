package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.wrap.model.Content;
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

    protected final Content getContent(final String content) {
        return Content.builder().content(content).build();
    }

    protected final AbstractContentWrapIterator getContentWrapperIterator(final String beanName, Object... args) {
        return (AbstractContentWrapIterator) applicationContext.getBean(beanName, args);
    }

    @Override
    public int getDefaultMaxLength() {
        return defaultMaxLength;
    }
}
