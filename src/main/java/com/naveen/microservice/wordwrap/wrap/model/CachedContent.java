package com.naveen.microservice.wordwrap.wrap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@AllArgsConstructor
@Builder
public class CachedContent implements Serializable {
    private final String id;
    private final Content content;
}
