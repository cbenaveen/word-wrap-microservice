package com.naveen.microservice.wordwrap.wrap;

public enum WrapperTypes {
    WORD_BY_WORD_WRAPPER (WordByWordSplitWrapper.class),
    CHAR_POSITION_BASED_CONTENT_WRAPPER(CharPositionBasedContentWrapper.class);

    private final Class<? extends AbstractContentWrapIterator> abstractContentWrapIterator;

    WrapperTypes(Class<? extends AbstractContentWrapIterator> abstractContentWrapIterator) {
        this.abstractContentWrapIterator = abstractContentWrapIterator;
    }

    public Class<? extends AbstractContentWrapIterator> getWrapperClass() {
        return this.abstractContentWrapIterator;
    }
}
