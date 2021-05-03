package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.model.CachedContent;
import com.naveen.microservice.wordwrap.model.WrappedContent;

import java.util.Collection;

public interface PersistentWordWrapService {
    CachedContent create(final String content, final int maxLength);
    CachedContent create(final String content);
    WrappedContent wrap(final long contentId, final int nextOffset, final int itemPerPage);
    WrappedContent wrap(final long contentId);
}
