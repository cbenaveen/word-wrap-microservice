package com.naveen.microservice.wordwrap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class WrappedResponse {
    protected final Collection<String> lines;
}
