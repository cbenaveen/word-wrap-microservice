package com.naveen.microservice.wordwrap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.microservice.wordwrap.controller.dto.ContentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WordWrapControllerIntegrationTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String CONTENT = "A Content for test Content Wrap By Passing Content Alone unit test case";
    private static final String CONTENT2 = "Develop a word wrap micro service. It should take an input string and return " +
            "a wrapped string so that none of the lines are longer than the max length. The lines should not break any " +
            "word in the middle. ";

    private static final String EXPECTED_ERROR_MESSAGE_FOR_INVALID_CONTENT = "{\"violations\":[{\"fieldName\":\"content\"," +
            "\"message\":\"The content that needs to be wrapped into lines should not be null or empty\"}]}";
    private static final String EXPECTED_ERROR_MESSAGE_FOR_INVALID_MAX_LENGTH = "{\"violations\":[{\"fieldName\":" +
            "\"maxLength\",\"message\":\"The max length of words should be greater than 0\"}]}";
    public static final String API_V_1_WRAP = "/api/v1/wrap";

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @DisplayName("Test invalid content input to the /api/v1/wrap URI")
    @NullAndEmptySource
    public void testNullAndEmptyContentSubmittedWrapEndpoint(String content) throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(content).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(EXPECTED_ERROR_MESSAGE_FOR_INVALID_CONTENT));
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
        ContentRequest contentRequest = ContentRequest.builder().content("A test input").build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(mediaType)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnsupportedMediaType());
    }

    @ParameterizedTest
    @DisplayName("Test valid content but invalid max length input to the /api/v1/wrap URI")
    @ValueSource(ints = {Integer.MIN_VALUE, -22, 0})
    public void testValidContentButInvalidMaxLengthSubmittedToWrapEndpoint(final int maxLength) throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(maxLength).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(EXPECTED_ERROR_MESSAGE_FOR_INVALID_MAX_LENGTH));

    }

    @Test
    @DisplayName("Test response of API with valid content and custom max length input to the /api/v1/wrap URI")
    public void testValidContentAndValidMaxLengthSubmittedToWrapEndpoint() throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(4).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("{\"lines\":[\"A \",\"Content\",\"for \",\"test\",\"Content\"" +
                        ",\"Wrap\",\"By \",\"Passing\",\"Content\",\"Alone\",\"unit\",\"test\",\"case\"]}"));
    }

    @Test
    @DisplayName("Test response of API with valid content but custom max length is not in the input to the /api/v1/wrap URI")
    public void testValidContentWithDefaultMaxLengthSubmittedToWrapEndpoint() throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("{\"lines\":[\"A Content for  \",\"test Content  \"," +
                        "\"Wrap By  \",\"Passing  \",\"Content Alone  \",\"unit test case \"]}"));
    }

//    @Test
    @DisplayName("Test response of API with valid content and custom max length input to the /api/v1/wrap's reactive endpoint")
    public void testValidContentAndCustomMaxLengthSubmittedToReactiveEndpoint() throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT).maxLength(4).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(content().string("data:A \n\ndata:Content\n\n" +
                        "data:for \n\ndata:test\n\ndata:Content\n\ndata:Wrap\n\n" +
                        "data:By \n\ndata:Passing\n\ndata:Content\n\ndata:Alone\n\n" +
                        "data:unit\n\ndata:test\n\ndata:case\n\n"));
    }

    @Test
    @DisplayName("Test response of API with valid content and custom max length input to the /api/v1/wrap's reactive endpoint")
    public void testValidContentAndDefaultMaxLengthSubmittedToReactiveEndpoint() throws Exception {
        ContentRequest contentRequest = ContentRequest.builder().content(CONTENT2).build();
        String contentRequestJsonString = mapper.writeValueAsString(contentRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
                .content(contentRequestJsonString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(content().string("data:Develop a word  \n\n" +
                        "data:wrap micro  \n\ndata:service. It  \n\ndata:should take an  \n\n" +
                        "data:input string  \n\ndata:and return a  \n\n" +
                        "data:wrapped string  \n\ndata:so that none  \n\n" +
                        "data:of the lines  \n\ndata:are longer  \n\n" +
                        "data:than the max  \n\ndata:length. The  \n\n" +
                        "data:lines should  \n\ndata:not break any  \n\n" +
                        "data:word in the  \n\ndata:middle. \n\n"));
    }

    @Test
    @DisplayName("Test response of /api/v1/wrap's OPTION HTTP call")
    public void testOptions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .options(API_V_1_WRAP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ALLOW, "GET,POST,DELETE,OPTIONS"));
    }
}
