package com.naveen.microservice.wordwrap.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ContentRequest {
    @NotBlank //@JsonProperty("content")
    private final String content;

    //@JsonProperty("max_length")
    private Integer maxLength;
}
