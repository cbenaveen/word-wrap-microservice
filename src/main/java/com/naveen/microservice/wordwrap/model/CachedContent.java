package com.naveen.microservice.wordwrap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@KeySpace("cached-content")
public class CachedContent implements Serializable {
    @Id
    private final long id;
    private final Content content;
    private final int maxLength;
    private int currentOffset;
}
