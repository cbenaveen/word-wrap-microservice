package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.model.CachedContent;
import com.naveen.microservice.wordwrap.model.WrappedContent;

import java.util.Collection;

public interface PersistentWordWrapService {
    CachedContent create(final String content);
    WrappedContent wrap(final long contentId, final int maxLength, final int itemsPerPage);
    WrappedContent wrap(final long contentId, final int maxLength);
    WrappedContent wrap(final long contentId);
}
