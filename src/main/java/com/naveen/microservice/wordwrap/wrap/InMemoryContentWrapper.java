package com.naveen.microservice.wordwrap.wrap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("inMemoryContentWrapper")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InMemoryContentWrapper extends AbstractContentWrapIterator {
    public InMemoryContentWrapper(final String content, int maxLength) {
        super(content, maxLength);
    }


}
