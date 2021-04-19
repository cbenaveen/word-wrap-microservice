package com.naveen.microservice.wordwrap.service;

import java.util.Collection;

public interface WordWrapService {
    Collection<String> wrap(final String content, final int maxLength);
    Collection<String> wrap(final String content);
}
