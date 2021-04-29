package com.naveen.microservice.wordwrap.service;

import reactor.core.publisher.Flux;

import java.util.Collection;

public interface WordWrapService {
    Collection<String> wrap(final String content, final int maxLength);
    Collection<String> wrap(final String content);
    Flux<String> reactive(final String content);
    Flux<String> reactive(final String content, final int maxLength);
}
