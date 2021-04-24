package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.WrapTypes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
class WrapServiceFactoryImpl implements WrapServiceFactory {
    private final ApplicationContext applicationContext;

    @Override
    public WordWrapService get(WrapTypes wrapTypes) {
        if (wrapTypes == WrapTypes.INMEMORY) {
            return applicationContext.getBean(WordWrapServiceImpl.class);
        } else if (wrapTypes == WrapTypes.PAGINATION) {
            return applicationContext.getBean(HazelcastCacheWordWrapService.class);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
