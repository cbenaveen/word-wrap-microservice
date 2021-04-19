package com.naveen.microservice.wordwrap.wrap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Getter
public abstract class AbstractContentWrapIterator implements Iterator<String> {
    protected static final String WORD_SPLIT_REGEX_PATTERN = "\\s+";
    private static final Pattern PATTERN = Pattern.compile(WORD_SPLIT_REGEX_PATTERN);

    private final String content;
    private final int maxLength;

    private String[] words;
    private int currentWordIndex = 0;

    protected AbstractContentWrapIterator(String content, int maxLength) {
        this.content = content;
        this.maxLength = maxLength;

        splitContent();
    }

    protected void splitContent() {
        if (Objects.nonNull(content)) {
            words = PATTERN.split(content);
        }
    }

    @Override
    public boolean hasNext() {
        if (Objects.isNull(words) || words.length <= 0) {
            return false;
        } else if (words.length == currentWordIndex) {
            return false;
        }

        return true;
    }

    @Override
    public String next() {
        StringBuilder returnLine = new StringBuilder();

        while(true) {
            if (currentWordIndex == words.length) {
                break;
            }

            String aWord = words[currentWordIndex];
            if(aWord.length() >= maxLength) {
                if (returnLine.toString().length() == 0) {
                    returnLine.append(aWord);
                    currentWordIndex += 1;
                }
                break;
            } else if (returnLine.length() + (" " + aWord).length() > maxLength) {
                returnLine.append(" ");
                break;
            } else {
                returnLine.append(aWord).append(" ");
                currentWordIndex += 1;
            }
        }

        return returnLine.toString();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("The remove method of this Iterator is not Supported.");
    }
}
