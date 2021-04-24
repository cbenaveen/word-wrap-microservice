package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.model.CachedContent;

import java.util.Collection;

public interface PersistentWordWrapService extends WordWrapService {
    CachedContent create(final String content);
    CachedContent get(final long id);
    int getDefaultItemsPerPage();
    Collection<String> wrap(final long contentId, final int maxLength, final int itemsPerPage);
    Collection<String> wrap(final long contentId, final int maxLength);
    Collection<String> wrap(final long contentId);
}
