package com.naveen.microservice.wordwrap.service.persistent;

import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import org.springframework.context.ApplicationContext;

public abstract class AbstractPersistentWordWrapService implements PersistentWordWrapService {
    protected final ApplicationContext applicationContext;
    protected final int defaultMaxLength;
    protected final int defaultItemsPerPage;

    public AbstractPersistentWordWrapService(final ApplicationContext applicationContext,
                                             final int defaultMaxLength,
                                             final int defaultItemsPerPage) {
        this.applicationContext = applicationContext;
        this.defaultMaxLength = defaultMaxLength;
        this.defaultItemsPerPage = defaultItemsPerPage;
    }
}
