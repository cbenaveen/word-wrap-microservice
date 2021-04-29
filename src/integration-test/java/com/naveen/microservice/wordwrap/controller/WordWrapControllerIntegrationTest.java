package com.naveen.microservice.wordwrap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.microservice.wordwrap.MeterRegistryTestConfiguration;
import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MeterRegistryTestConfiguration.class)
@WebMvcTest(value = WordWrapController.class)
public class WordWrapControllerIntegrationTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordWrapService wordWrapService;

    @MockBean
    private PersistentWordWrapService persistentWordWrapService;

    @Test
    @DisplayName("Test invalid content input to the /api/v1/wrap URI")
    public void testNullContentSubmittedWrapEndpoint() throws Exception {
        final String expectedErrorMessage = "{\"violations\":[{\"fieldName\":\"content\",\"message\":\"must not be blank\"}]}";
        ContentRequest contentRequest = ContentRequest.builder().build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/wrap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                //Uncomment the below line to get good logging about your test request and response
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));

        verifyZeroInteractionWithServiceObjects();
    }

    @Test
    @DisplayName("Test empty content input to the /api/v1/wrap URI")
    public void testEmptyContentSubmittedWrapEndpoint() throws Exception {
        final String expectedErrorMessage = "{\"violations\":[{\"fieldName\":\"content\",\"message\":\"must not be blank\"}]}";
        ContentRequest contentRequest = ContentRequest.builder().content("").build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/wrap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));

        verifyZeroInteractionWithServiceObjects();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            MediaType.ALL_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_CBOR_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_NDJSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE,
            MediaType.APPLICATION_PROBLEM_JSON_VALUE, MediaType.APPLICATION_PROBLEM_XML_VALUE, MediaType.APPLICATION_RSS_XML_VALUE,
            MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_MIXED_VALUE, MediaType.MULTIPART_RELATED_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE,
            MediaType.TEXT_HTML_VALUE, MediaType.TEXT_MARKDOWN_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_XML_VALUE
    })
    @DisplayName("Test to make sure that the request with Content-Type set to NON application/json returns with 415 errors")
    public void testInvalidContentTypeSetInRequest(final String mediaType) throws Exception {
        final String expectedErrorMessage = "{\"violations\":[{\"fieldName\":\"content\",\"message\":\"must not be blank\"}]}";
        ContentRequest contentRequest = ContentRequest.builder().content("A test input").build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/wrap")
                .contentType(mediaType)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractionWithServiceObjects();
    }

    /**
     * In any of the error scenarios, the service apis should not be called by the controller implementation.
     */
    private void verifyZeroInteractionWithServiceObjects() {
        verify(wordWrapService, never()).wrap(anyString(), anyInt());
        verify(wordWrapService, never()).wrap(anyString());
        verify(wordWrapService, never()).reactive(anyString(), anyInt());
        verify(wordWrapService, never()).reactive(anyString());
        verify(persistentWordWrapService, never()).create(anyString());
        verify(persistentWordWrapService, never()).wrap(anyLong());
        verify(persistentWordWrapService, never()).wrap(anyLong(), anyInt(), anyInt());
        verify(persistentWordWrapService, never()).wrap(anyLong(), anyInt());
    }
}
