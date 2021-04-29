package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.model.CachedContent;

import java.util.Collection;

public interface PersistentWordWrapService {
    CachedContent create(final String content);
    Collection<String> wrap(final long contentId, final int maxLength, final int itemsPerPage);
    Collection<String> wrap(final long contentId, final int maxLength);
    Collection<String> wrap(final long contentId);
}
