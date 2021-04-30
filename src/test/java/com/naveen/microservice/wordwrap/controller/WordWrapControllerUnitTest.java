package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.model.CachedContent;
import com.naveen.microservice.wordwrap.model.Content;
import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordWrapControllerUnitTest {
    private static final int DEFAULT_MAX_LENGTH = 15;
    private static final int DEFAULT_PAGE_ITEM_SIZE = 15;

    @Mock
    private WordWrapService wordWrapService;
    @Mock
    private PersistentWordWrapService persistentWordWrapService;
    @Mock
    private MeterRegistry meterRegistry;

    @Captor
    ArgumentCaptor<String> contentCapture;
    @Captor
    ArgumentCaptor<Integer> maxLengthCapture;
    @Captor
    ArgumentCaptor<Integer> idCapture;
    @Captor
    ArgumentCaptor<Integer> pageSizeCapture;

    private WordWrapController wordWrapController;

    @BeforeEach
    public void setup() {
        wordWrapController = new WordWrapController(DEFAULT_MAX_LENGTH, DEFAULT_PAGE_ITEM_SIZE,
                wordWrapService, persistentWordWrapService, meterRegistry);
        when(meterRegistry.counter(anyString(), ArgumentMatchers.<String>any())).thenReturn(mock(Counter.class));
    }

    @Test
    public void testProcessContentWrapWithoutPaginationAndWithoutMaxLength() {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).build();
        wordWrapController.processContentWrap(contentRequest, false, 0);

        verifyNoInteractions(persistentWordWrapService);
        verify(wordWrapService, times(1)).wrap(contentCapture.capture(), maxLengthCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(DEFAULT_MAX_LENGTH, maxLengthCapture.getValue());
    }

    @Test
    public void testProcessContentWrapReactiveWithDefaultMaxLength() {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).build();
        wordWrapController.processContentWrapReactive(contentRequest);

        verifyNoInteractions(persistentWordWrapService);
        verify(wordWrapService, times(1)).reactive(contentCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
    }

    @Test
    public void testProcessContentWrapReactiveWithCustomMaxLength() {
        final int CUSTOM_MAX_LENGTH = 23;
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(CUSTOM_MAX_LENGTH).build();
        wordWrapController.processContentWrapReactive(contentRequest);

        verifyNoInteractions(persistentWordWrapService);
        verify(wordWrapService, times(1)).reactive(contentCapture.capture(), maxLengthCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(CUSTOM_MAX_LENGTH, maxLengthCapture.getValue());
    }

    @Test
    public void testProcessContentWrapWithoutPaginationAndWithCustomMaxLength() {
        final int CUSTOM_MAX_LENGTH = 23;
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(CUSTOM_MAX_LENGTH).build();
        wordWrapController.processContentWrap(contentRequest, false, 0);

        verifyNoInteractions(persistentWordWrapService);
        verify(wordWrapService, times(1)).wrap(contentCapture.capture(), maxLengthCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(CUSTOM_MAX_LENGTH, maxLengthCapture.getValue());
    }

    @Test
    public void testProcessContentWrapWithDefaultPaginationAndDefaultMaxLength() {
        CachedContent mock = getMockCachedContent(CONTENT);
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).build();

        when(persistentWordWrapService.create(anyString())).thenReturn(mock);

        wordWrapController.processContentWrap(contentRequest, true, 0);

        verifyNoInteractions(wordWrapService);
        verify(persistentWordWrapService, times(1)).create(contentCapture.capture());
        verify(persistentWordWrapService, times(1)).wrap(idCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
    }

    @Test
    public void testProcessContentWrapWithDefaultPaginationAndCustomMaxLength() {
        final int maxLength = 10;
        CachedContent mock = getMockCachedContent(CONTENT);
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(maxLength).build();

        when(persistentWordWrapService.create(anyString())).thenReturn(mock);

        wordWrapController.processContentWrap(contentRequest, true, 0);

        verifyNoInteractions(wordWrapService);
        verify(persistentWordWrapService, times(1)).create(contentCapture.capture());
        verify(persistentWordWrapService, times(1)).wrap(idCapture.capture(),
                maxLengthCapture.capture(), pageSizeCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(maxLength, maxLengthCapture.getValue());
        assertEquals(DEFAULT_PAGE_ITEM_SIZE, pageSizeCapture.getValue());
    }

    @Test
    public void testProcessContentWrapWithCustomPaginationAndCustomMaxLength() {
        final int maxLength = 10;
        final int customPage = 3;
        CachedContent mock = getMockCachedContent(CONTENT);
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(maxLength).build();

        when(persistentWordWrapService.create(anyString())).thenReturn(mock);

        wordWrapController.processContentWrap(contentRequest, true, customPage);

        verifyNoInteractions(wordWrapService);
        verify(persistentWordWrapService, times(1)).create(contentCapture.capture());
        verify(persistentWordWrapService, times(1)).wrap(idCapture.capture(),
                maxLengthCapture.capture(), pageSizeCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(maxLength, maxLengthCapture.getValue());
        assertEquals(customPage, pageSizeCapture.getValue());
    }

    @Test
    public void testProcessContentWrapWithCustomPaginationAndDefaultMaxLength() {
        final int customPage = 3;
        CachedContent mock = getMockCachedContent(CONTENT);
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).build();

        when(persistentWordWrapService.create(anyString())).thenReturn(mock);

        wordWrapController.processContentWrap(contentRequest, true, customPage);

        verifyNoInteractions(wordWrapService);
        verify(persistentWordWrapService, times(1)).create(contentCapture.capture());
        verify(persistentWordWrapService, times(1)).wrap(idCapture.capture(),
                maxLengthCapture.capture(), pageSizeCapture.capture());
        assertEquals(CONTENT, contentCapture.getValue());
        assertEquals(DEFAULT_MAX_LENGTH, maxLengthCapture.getValue());
        assertEquals(customPage, pageSizeCapture.getValue());
    }

    private CachedContent getMockCachedContent(final String content) {
        Content build = Content.builder().content(content).build();
        return CachedContent.builder().content(build).id(1l).build();
    }

    private static final String CONTENT = "A Content for test Content Wrap By Passing Content Alone unit test case";
}
