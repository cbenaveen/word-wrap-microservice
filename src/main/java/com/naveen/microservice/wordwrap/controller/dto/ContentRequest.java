package com.naveen.microservice.wordwrap.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ContentRequest {
    @NotBlank(message = "The content that needs to be wrapped into lines should not be null or empty")
    private final String content;
    @Positive(message = "The max length of words should be greater than 0")
    private Integer maxLength;
}
