package com.naveen.microservice.wordwrap.wrap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
public class Content implements Serializable {
    private final String content;
    private final String[] words;

    @Getter @Setter
    private int currentWordIndex = 0;
}
