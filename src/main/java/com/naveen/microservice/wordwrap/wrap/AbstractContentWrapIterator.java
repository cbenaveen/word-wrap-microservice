package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.wrap.model.Content;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Getter
public abstract class AbstractContentWrapIterator implements Iterator<String>, Iterable<String> {
    protected static final String WORD_SPLIT_REGEX_PATTERN = "\\s+";
    private static final Pattern PATTERN = Pattern.compile(WORD_SPLIT_REGEX_PATTERN);

    private final Content content;
    private final int maxLength;

    protected AbstractContentWrapIterator(String content, int maxLength) {
        Content.ContentBuilder contentBuilder = Content.builder().content(content);
        if (Objects.nonNull(content)) {
            contentBuilder.words(PATTERN.split(content));
        }

        this.content = contentBuilder.build();
        this.maxLength = maxLength;
    }

    @Override
    public boolean hasNext() {
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
