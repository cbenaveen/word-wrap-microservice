package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.controller.dto.WrappedResponse;
import com.naveen.microservice.wordwrap.service.WrapServiceFactory;
import com.naveen.microservice.wordwrap.wrap.WrapTypes;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RestController
@Slf4j
@AllArgsConstructor
public class WordWrapController {
    private static final String BASE_API_BATH = "/api/v1/wrap";
    private static final String METRIC_NAME_CONTENT_WRAP_REQUEST = "content.wrap.request";
    private static final String TAG_KEY_REQUEST_TYPE = "request-type";
    private static final String TAG_KEY_API_PATH = "api-path";
    private static final String TAG_NON_REACTIVE = "non-reactive";
    private static final String TAG_REACTIVE = "reactive";

    private final WrapServiceFactory wrapServiceFactory;
    private final MeterRegistry meterRegistry;

    @PostMapping(value = BASE_API_BATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<WrappedResponse> processContentWrap(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Request received with Paragraph object {}", contentRequest);

        Collection<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wrapServiceFactory.get(WrapTypes.INMEMORY).wrap(contentRequest.getContent())
                : wrapServiceFactory.get(WrapTypes.INMEMORY).wrap(contentRequest.getContent(), contentRequest.getMaxLength());

        WrappedResponse wrappedResponse = WrappedResponse.builder().lines(wrap).build();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(wrappedResponse, HttpStatus.OK);

        this.meterRegistry.counter(METRIC_NAME_CONTENT_WRAP_REQUEST,
                TAG_KEY_REQUEST_TYPE, TAG_NON_REACTIVE, TAG_KEY_API_PATH, BASE_API_BATH).increment();

        return wrappedResponseResponseEntity;
    }

    @PostMapping(value = BASE_API_BATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> processContentWrapReactive(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Content Wrap Reactive: Request received with Paragraph object {}", contentRequest);

        Flux<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wrapServiceFactory.get(WrapTypes.INMEMORY).reactive(contentRequest.getContent())
                : wrapServiceFactory.get(WrapTypes.INMEMORY).reactive(contentRequest.getContent(), contentRequest.getMaxLength());

        this.meterRegistry.counter(METRIC_NAME_CONTENT_WRAP_REQUEST,
                TAG_KEY_REQUEST_TYPE, TAG_REACTIVE, TAG_KEY_API_PATH, BASE_API_BATH).increment();

        return wrap;
    }

    @RequestMapping(value=BASE_API_BATH, method = RequestMethod.OPTIONS)
    private ResponseEntity<?> generateOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }
}
