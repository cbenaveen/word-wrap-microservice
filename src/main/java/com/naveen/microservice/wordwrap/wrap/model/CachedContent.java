package com.naveen.microservice.wordwrap.wrap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
public class CachedContent implements Serializable {
    private final long id;
    private final Content content;
    private int totalPage;
    private int currentPage;
}
