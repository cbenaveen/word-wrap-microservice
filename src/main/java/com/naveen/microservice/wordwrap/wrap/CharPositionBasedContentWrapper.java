package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.model.Content;
import org.springframework.util.ObjectUtils;

public class CharPositionBasedContentWrapper extends AbstractContentWrapIterator {
    private final String rawContent;
    private final int contentLength;

    private int currentCharIndexPosition;

    protected CharPositionBasedContentWrapper(Content content, int maxLength,
                                              int currentCharIndex) {
        super(content, maxLength);

        this.rawContent = content.getContent();
        if (!ObjectUtils.isEmpty(rawContent)) {
            this.contentLength = content.getContent().length();
        } else {
            this.contentLength = -1;
        }
        this.currentCharIndexPosition = currentCharIndex;
    }

    @Override
    public boolean hasNext() {
        if (contentLength <= 0 || currentCharIndexPosition < 0) {
            return false;
        }

        if (currentCharIndexPosition >= contentLength) {
            return false;
        }

        return true;
    }

    @Override
    public String next() {
        StringBuilder returnLine = new StringBuilder();

        while(hasNext()) {
            StringBuilder aWord = extracted();

            int wordLength = aWord.length();
            if ((returnLine.length() + wordLength) < maxLength) {
                returnLine.append(aWord);
                currentCharIndexPosition += wordLength;
            } else if (returnLine.length() == 0) {
                returnLine.append(aWord);
                currentCharIndexPosition += wordLength;
                break;
            } else {
                break;
            }
        }

        return returnLine.toString();
    }

    private StringBuilder extracted() {
        StringBuilder aWord = new StringBuilder();
        int wordCharPosition = currentCharIndexPosition;
        while(wordCharPosition < contentLength) {
            aWord.append((char) rawContent.codePointAt(wordCharPosition));
            if (Character.isWhitespace(rawContent.codePointAt(wordCharPosition))) {
                break;
            }
            wordCharPosition += 1;
        }

        return aWord;
    }
}
