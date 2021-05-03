package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import com.naveen.microservice.wordwrap.model.Content;
import com.naveen.microservice.wordwrap.wrap.WrapperTypes;
import org.springframework.context.ApplicationContext;

public class Utils {
    public static final Content getContent(final String content) {
        return Content.builder().content(content).build();
    }

    public static final AbstractContentWrapIterator getContentWrapperIterator(final ApplicationContext applicationContext,
                                                                              WrapperTypes types, Object... args) {
        return applicationContext.getBean(types.getWrapperClass(), args);
    }
}