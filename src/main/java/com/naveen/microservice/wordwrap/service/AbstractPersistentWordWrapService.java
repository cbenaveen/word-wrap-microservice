package com.naveen.microservice.wordwrap.service;

import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.Collection;

public abstract class AbstractPersistentWordWrapService extends AbstractWordWrapService
        implements PersistentWordWrapService {
    protected final int defaultItemsPerPage;

    public AbstractPersistentWordWrapService(ApplicationContext applicationContext, int defaultMaxLength, int defaultItemsPerPage) {
        super(applicationContext, defaultMaxLength);
        this.defaultItemsPerPage = defaultItemsPerPage;
    }

    @Override
    public Collection<String> wrap(String content, int maxLength) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Collection<String> wrap(String content) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Flux<String> reactive(String content) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Flux<String> reactive(String content, int maxLength) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public int getDefaultItemsPerPage() {
        return defaultItemsPerPage;
    }
}
