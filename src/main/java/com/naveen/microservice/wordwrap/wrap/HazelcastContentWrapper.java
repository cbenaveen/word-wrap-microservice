package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hazelcastContentWrapper")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HazelcastContentWrapper extends AbstractContentWrapIterator {
    @Autowired
    protected HazelcastContentWrapper(final PersistentWordWrapService persistentWordWrapService,
                                      final long contentId, int maxLength) {
        super(persistentWordWrapService.get(contentId).getContent(), maxLength);
    }
}
