package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.model.Content;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("inMemoryContentWrapper")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class InMemoryContentWrapper extends AbstractContentWrapIterator {
    InMemoryContentWrapper(final Content content, int maxLength) {
        super(content, maxLength);
    }
}
