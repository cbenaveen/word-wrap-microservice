package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.controller.dto.PaginatedWrappedResponse;
import com.naveen.microservice.wordwrap.controller.dto.WrappedResponse;
import com.naveen.microservice.wordwrap.model.WrappedContent;
import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import com.naveen.microservice.wordwrap.model.CachedContent;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Objects;

@RestController
@Slf4j
@Timed
public class WordWrapController {
    private static final String BASE_API_PATH = "/api/v1/wrap";
    private static final String PAGINATED_API_PATH = "/api/v1/wrap/{id}";
    private static final String METRIC_NAME_CONTENT_WRAP_REQUEST = "content.wrap.request";
    private static final String TAG_KEY_REQUEST_TYPE = "request-type";
    private static final String TAG_KEY_API_PATH = "api-path";
    private static final String TAG_NON_REACTIVE = "non-reactive";
    private static final String TAG_REACTIVE = "reactive";

    private final int defaultMaxLength;
    private final int defaultItemsPerPage;
    private final WordWrapService wordWrapService;
    private final PersistentWordWrapService persistentWordWrapService;
    private final MeterRegistry meterRegistry;

    public WordWrapController(@Value("${default.max.length:15}") int defaultMaxLength,
                              @Value("${default.items.per.page:3}") int defaultItemsPerPage,
                              WordWrapService wordWrapService,
                              PersistentWordWrapService persistentWordWrapService,
                              MeterRegistry meterRegistry) {
        this.defaultMaxLength = defaultMaxLength;
        this.defaultItemsPerPage = defaultItemsPerPage;
        this.wordWrapService = wordWrapService;
        this.persistentWordWrapService = persistentWordWrapService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping(value = BASE_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected final ResponseEntity<WrappedResponse> processContentWrap(@Valid @RequestBody final ContentRequest contentRequest,
                                                               @RequestParam(value = "paginate", defaultValue = "false", required = false) final boolean paginate,
                                                               @RequestParam(value = "itemsPerPage", defaultValue = "0", required = false) final int itemsPerPage) {
        log.info("Request received with Paragraph object {}, pagination set to {}", contentRequest, paginate);
        return (paginate) ? getPaginatedWrappedResponseResponseEntity(contentRequest, itemsPerPage)
                : getWrappedResponseResponseEntity(contentRequest);
    }

    protected final ResponseEntity<WrappedResponse> getPaginatedWrappedResponseResponseEntity(ContentRequest contentRequest, int itemsPerPage) {
        CachedContent cachedContent = persistentWordWrapService.create(contentRequest.getContent());

        this.meterRegistry.counter(METRIC_NAME_CONTENT_WRAP_REQUEST,
                TAG_KEY_REQUEST_TYPE, TAG_NON_REACTIVE, TAG_KEY_API_PATH, BASE_API_PATH).increment();

        WrappedContent wrap;
        if ((Objects.nonNull(contentRequest.getMaxLength()) && contentRequest.getMaxLength() > 0) && itemsPerPage > 0) {
            wrap = persistentWordWrapService.wrap(cachedContent.getId(), contentRequest.getMaxLength(), itemsPerPage);
        } else if ((Objects.nonNull(contentRequest.getMaxLength()) && contentRequest.getMaxLength() > 0) && itemsPerPage <= 0) {
            wrap = persistentWordWrapService.wrap(cachedContent.getId(), contentRequest.getMaxLength(), defaultItemsPerPage);
        } else if ((Objects.isNull(contentRequest.getMaxLength()) || contentRequest.getMaxLength() <= 0) && itemsPerPage > 0) {
            wrap = persistentWordWrapService.wrap(cachedContent.getId(), defaultMaxLength, itemsPerPage);
        } else {
            wrap = persistentWordWrapService.wrap(cachedContent.getId());
        }

        PaginatedWrappedResponse paginatedWrappedResponse = PaginatedWrappedResponse.paginatedBuilder().lines(wrap.getLines())
                .contentId(wrap.getContentId()).nextOffset(cachedContent.getCurrentOffset()).paginatedBuilder();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(paginatedWrappedResponse,
                HttpStatus.OK);

        return wrappedResponseResponseEntity;
    }

    protected final ResponseEntity<WrappedResponse> getWrappedResponseResponseEntity(ContentRequest contentRequest) {
        Collection<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wordWrapService.wrap(contentRequest.getContent(), defaultMaxLength)
                : wordWrapService.wrap(contentRequest.getContent(), contentRequest.getMaxLength());

        WrappedResponse wrappedResponse = WrappedResponse.builder().lines(wrap).build();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(wrappedResponse, HttpStatus.OK);

        this.meterRegistry.counter(METRIC_NAME_CONTENT_WRAP_REQUEST,
                TAG_KEY_REQUEST_TYPE, TAG_NON_REACTIVE, TAG_KEY_API_PATH, BASE_API_PATH).increment();

        return wrappedResponseResponseEntity;
    }

    @PostMapping(value = BASE_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    protected final Flux<String> processContentWrapReactive(@Valid @RequestBody final ContentRequest contentRequest) {
        log.info("Content Wrap Reactive: Request received with Paragraph object {}", contentRequest);

        Flux<String> wrap = (Objects.isNull(contentRequest.getMaxLength()))
                ? wordWrapService.reactive(contentRequest.getContent())
                : wordWrapService.reactive(contentRequest.getContent(), contentRequest.getMaxLength());

        this.meterRegistry.counter(METRIC_NAME_CONTENT_WRAP_REQUEST,
                TAG_KEY_REQUEST_TYPE, TAG_REACTIVE, TAG_KEY_API_PATH, BASE_API_PATH).increment();

        return wrap;
    }

    @RequestMapping(value= BASE_API_PATH, method = RequestMethod.OPTIONS)
    protected final ResponseEntity<?> generateOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping(value = PAGINATED_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    protected final ResponseEntity<WrappedResponse> paginatedWrapperHandler(@Min(0) @PathVariable long id,
                                                                            @RequestParam(value = "offset", defaultValue = "0", required = false) final int offset) {
        WrappedContent wrap = persistentWordWrapService.wrap(id, offset, defaultItemsPerPage);

        PaginatedWrappedResponse paginatedWrappedResponse = PaginatedWrappedResponse.paginatedBuilder()
                .contentId(wrap.getContentId()).lines(wrap.getLines()).nextOffset(wrap.getCurrentPosition()).paginatedBuilder();
        ResponseEntity<WrappedResponse> wrappedResponseResponseEntity =  new ResponseEntity(paginatedWrappedResponse,
                HttpStatus.OK);

        return wrappedResponseResponseEntity;
    }

    @DeleteMapping(value = PAGINATED_API_PATH)
    protected final ResponseEntity deleteContent(@Min(0) @PathVariable long id) {
        CachedContent wrap = persistentWordWrapService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
