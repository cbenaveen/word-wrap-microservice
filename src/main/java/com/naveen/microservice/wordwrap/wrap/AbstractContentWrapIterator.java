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
    public boolean hasNext() {
        if (Objects.isNull(content) || maxLength <= 0) {
            throw new IllegalArgumentException("The Content POJO set to the iterator is null");
        }

        if (Objects.isNull(content.getWords()) || content.getWords().length <= 0) {
            return false;
        } else if (content.getWords().length == content.getCurrentWordIndex()) {
            return false;
        }

        return true;
    }

    @Override
    public String next() {
        StringBuilder returnLine = new StringBuilder();

        while(true) {
            if (content.getCurrentWordIndex() == content.getWords().length) {
                break;
            }

            String aWord = content.getWords()[content.getCurrentWordIndex()];
            if(aWord.length() >= maxLength) {
                if (returnLine.toString().length() == 0) {
                    returnLine.append(aWord);
                    content.setCurrentWordIndex(content.getCurrentWordIndex() + 1);
                }
                break;
            } else if (returnLine.length() + (" " + aWord).length() > maxLength) {
                returnLine.append(" ");
                break;
            } else {
                returnLine.append(aWord).append(" ");
                content.setCurrentWordIndex(content.getCurrentWordIndex() + 1);
            }
        }

        return returnLine.toString();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("The remove method of this Iterator is not Supported.");
    }
}
