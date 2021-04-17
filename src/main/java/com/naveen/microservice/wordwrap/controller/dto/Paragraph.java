package com.naveen.microservice.wordwrap.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class Paragraph {
    @NotBlank
    private final String paragraph;
    private int maxSize;
}
