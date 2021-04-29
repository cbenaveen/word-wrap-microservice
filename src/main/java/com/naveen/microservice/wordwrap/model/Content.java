package com.naveen.microservice.wordwrap.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class Content implements Serializable {
    private final String content;
    private final String[] words;

    @Getter @Setter
    private int currentWordIndex = 0;

    private Content(String content, String[] words) {
        this.content = content;
        this.words = words;
    }

    public static ContentBuilder builder() {
        return new ContentBuilder();
    }

    public static class ContentBuilder {
        private static final String WORD_SPLIT_REGEX_PATTERN = "\\s+";
        private static final Pattern PATTERN = Pattern.compile(WORD_SPLIT_REGEX_PATTERN);

        private String content;

        ContentBuilder() {
        }

        public ContentBuilder content(String content) {
            this.content = content;
            return this;
        }

        public Content build() {
            if (Objects.isNull(content)) {
                throw new IllegalArgumentException("Content set in this model is invalid");
            }
            return new Content(content, PATTERN.split(content));
        }
    }
}
