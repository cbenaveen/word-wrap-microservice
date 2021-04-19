package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.controller.dto.WrappedResponse;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RestController
@Slf4j
@AllArgsConstructor
public class WordWrapController {
    private static final String BASE_API_BATH = "/api/v1/wrap";

    private final WordWrapService wordWrapService;

    @PostMapping(value = BASE_API_BATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<WrappedResponse> processContentWrap(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Request received with Paragraph object {}", contentRequest);

        Collection<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wordWrapService.wrap(contentRequest.getContent())
                : wordWrapService.wrap(contentRequest.getContent(), contentRequest.getMaxLength());

        WrappedResponse wrappedResponse = WrappedResponse.builder().lines(wrap).build();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(wrappedResponse, HttpStatus.OK);

        return wrappedResponseResponseEntity;
    }

    @PostMapping(value = BASE_API_BATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> processContentWrapReactive(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Content Wrap Reactive: Request received with Paragraph object {}", contentRequest);

        Flux<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wordWrapService.reactive(contentRequest.getContent())
                : wordWrapService.reactive(contentRequest.getContent(), contentRequest.getMaxLength());

        return wrap;
    }
}
