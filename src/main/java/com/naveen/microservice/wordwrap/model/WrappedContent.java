package com.naveen.microservice.wordwrap.model;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
public class WrappedContent {
    private Collection<String> lines;
    private int currentPosition;
}
