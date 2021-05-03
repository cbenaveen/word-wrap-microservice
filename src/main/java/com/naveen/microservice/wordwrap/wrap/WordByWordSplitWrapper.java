package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.model.Content;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class WordByWordSplitWrapper extends AbstractContentWrapIterator {
    WordByWordSplitWrapper(final Content content, int maxLength) {
        super(content, maxLength);
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
}
