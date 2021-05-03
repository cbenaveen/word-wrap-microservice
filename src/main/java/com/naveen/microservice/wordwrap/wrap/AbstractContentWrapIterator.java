package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.model.Content;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Objects;

@Slf4j
@Getter
public abstract class AbstractContentWrapIterator implements Iterator<String>, Iterable<String> {
    protected final Content content;
    protected final int maxLength;

    protected AbstractContentWrapIterator(final Content content, int maxLength) {
        this.content = content;
        this.maxLength = maxLength;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("The remove method of this Iterator is not Supported.");
    }

    public int currentPosition() {
        return content.getContent().length();
    }
}
