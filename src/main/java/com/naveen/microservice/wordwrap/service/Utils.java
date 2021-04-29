package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.model.Content;
import org.springframework.context.ApplicationContext;

public class Utils {
    private static final String IN_MEMORY_CONTENT_WRAPPER = "inMemoryContentWrapper";

    public static final Content getContent(final String content) {
        return Content.builder().content(content).build();
    }

    public static final AbstractContentWrapIterator getContentWrapperIterator(final ApplicationContext applicationContext,
                                                                              Object... args) {
        return (AbstractContentWrapIterator) applicationContext.getBean(IN_MEMORY_CONTENT_WRAPPER, args);
    }
}