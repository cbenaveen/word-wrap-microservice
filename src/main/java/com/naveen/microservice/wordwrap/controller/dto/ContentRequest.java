package com.naveen.microservice.wordwrap.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ContentRequest {
    @NotBlank
    private final String content;
    private Integer maxLength = null;
}
