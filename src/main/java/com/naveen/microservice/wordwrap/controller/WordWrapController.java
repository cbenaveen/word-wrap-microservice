package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.controller.dto.WrappedResponse;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RestController
@Slf4j
@AllArgsConstructor
public class WordWrapController {
    private static final String BASE_API_BATH = "/api/v1/wrap";

    private final WordWrapService wordWrapService;

    @GetMapping(value = BASE_API_BATH, consumes = "application/json")
    private ResponseEntity<WrappedResponse> test(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Request received with Paragraph object {}", contentRequest);

        Collection<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wordWrapService.wrap(contentRequest.getContent())
                : wordWrapService.wrap(contentRequest.getContent(), contentRequest.getMaxLength());

        WrappedResponse wrappedResponse = WrappedResponse.builder().lines(wrap).build();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(wrappedResponse, HttpStatus.OK);

        return wrappedResponseResponseEntity;
    }
}
