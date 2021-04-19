package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
class WordWrapServiceImpl implements WordWrapService {
    private final ApplicationContext applicationContext;
    private final int defaultMaxLength;

    WordWrapServiceImpl(ApplicationContext applicationContext,
                        @Value("${default.max.length:15}") int defaultMaxLength) {
        this.applicationContext = applicationContext;
        this.defaultMaxLength = defaultMaxLength;
    }

    @Override
    public Collection<String> wrap(String content, int maxLength) {
        AbstractContentWrapIterator inMemoryContentWrap = (AbstractContentWrapIterator)
                applicationContext.getBean("inMemoryContentWrapper", content, maxLength);

        List<String> lines = new ArrayList();

        while(inMemoryContentWrap.hasNext()) {
            lines.add(inMemoryContentWrap.next());
        }

        return lines;
    }

    @Override
    public Collection<String> wrap(String charBuffer) {
        return wrap(charBuffer, defaultMaxLength);
    }
}
