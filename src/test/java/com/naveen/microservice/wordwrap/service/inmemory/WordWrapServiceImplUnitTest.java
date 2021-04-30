package com.naveen.microservice.wordwrap.service.inmemory;

import com.naveen.microservice.wordwrap.model.Content;
import com.naveen.microservice.wordwrap.service.WordWrapService;
import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordWrapServiceImplUnitTest {
    private static final int DEFAULT_MAX_LENGTH = 10;

    @Mock
    private ApplicationContext mockApplicationContext;
    @Mock
    private AbstractContentWrapIterator mockAbstractContentWrapIterator;
    @Captor
    private ArgumentCaptor<String> beanNameCapture;
    @Captor
    private ArgumentCaptor<Object> varArgsCapture;

    private WordWrapService wordWrapService;

    @BeforeEach
    public void setup() {
        wordWrapService = new WordWrapServiceImpl(mockApplicationContext, DEFAULT_MAX_LENGTH);
        when(mockApplicationContext.getBean(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(mockAbstractContentWrapIterator);
    }

    @Test
    @DisplayName("Test to verify the content and the default max supplied to iterator is correct or not")
    public void testMaxLengthValueSetToDefaultToIterator() {
        final String content = "A test data";

        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(Boolean.FALSE);
        wordWrapService.wrap(content);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals("inMemoryContentWrapper", beanNameCapture.getValue());
        assertTrue(varArgsCapture.getAllValues().get(0) instanceof Content);
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(DEFAULT_MAX_LENGTH, varArgsCapture.getAllValues().get(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30, 45, 89, Integer.MAX_VALUE})
    @DisplayName("Test to verify the content and the custom default max supplied to iterator is correct or not")
    public void testMaxLengthValueSetToCustomValueToIterator(Integer maxLength) {
        final String content = "A test data";

        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(Boolean.FALSE);
        wordWrapService.wrap(content, maxLength);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals("inMemoryContentWrapper", beanNameCapture.getValue());
        assertTrue(varArgsCapture.getAllValues().get(0) instanceof Content);
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(maxLength, varArgsCapture.getAllValues().get(1));
    }

    @Test
    @DisplayName("Test to verify the content and the default max supplied to iterator is correct or not")
    public void testReactiveMaxLengthValueSetToDefaultToIterator() {
        final String content = "A test data";

        wordWrapService.reactive(content);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals("inMemoryContentWrapper", beanNameCapture.getValue());
        assertTrue(varArgsCapture.getAllValues().get(0) instanceof Content);
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(DEFAULT_MAX_LENGTH, varArgsCapture.getAllValues().get(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30, 45, 89, Integer.MAX_VALUE})
    @DisplayName("Test to verify the content and the custom default max supplied to iterator is correct or not")
    public void testReactiveMaxLengthValueSetToCustomValueToIterator(Integer maxLength) {
        final String content = "A test data";

        wordWrapService.reactive(content, maxLength);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals("inMemoryContentWrapper", beanNameCapture.getValue());
        assertTrue(varArgsCapture.getAllValues().get(0) instanceof Content);
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(maxLength, varArgsCapture.getAllValues().get(1));
    }

    @Test
    @DisplayName("Test to verify the response of wrap call")
    public void testWrapMethodCallReturnResult() {
        final String content = "A test data";
        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        when(mockAbstractContentWrapIterator.next()).thenReturn("A", "test data");

        Collection<String> collection = wordWrapService.wrap(content, DEFAULT_MAX_LENGTH);

        verify(mockAbstractContentWrapIterator, times(3)).hasNext();
        verify(mockAbstractContentWrapIterator, times(2)).next();
        assertEquals(2, collection.size());
        assertEquals(Arrays.asList("A", "test data"), collection);
    }

    @Test
    @DisplayName("Test to verify the response of wrap call")
    public void testWrapMethodCallReturnResultWithLongerContent() {
        String[] brokenWords = {"Develop a  ", "word wrap  ", "micro  ", "service.  ", "It should  ", "take an  ",
                "input  ", "string  ", "and  ", "return a  ", "wrapped  ", "string so  ", "that none  ", "of the  ",
                "lines are  ", "longer  ", "than the  ", "max  ", "length.  ", "The lines  ", "should  ", "not break  ",
                "any word  ", "in the  ", "middle. "};
        Boolean[] booleans = new Boolean[brokenWords.length + 1];
        for (int i = 0; i < brokenWords.length; i++) {
            booleans[i] = Boolean.TRUE;
        }
        booleans[booleans.length - 1] = Boolean.FALSE;

        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(booleans[0], Arrays.copyOfRange(booleans, 1, booleans.length));
        when(mockAbstractContentWrapIterator.next()).thenReturn(brokenWords[0], Arrays.copyOfRange(brokenWords, 1, brokenWords.length));

        Collection<String> collection = wordWrapService.wrap(CONTENT, DEFAULT_MAX_LENGTH);

        verify(mockAbstractContentWrapIterator, times(booleans.length)).hasNext();
        verify(mockAbstractContentWrapIterator, times(brokenWords.length)).next();
        assertEquals(brokenWords.length, collection.size());
        assertEquals(Arrays.asList(brokenWords), collection);
    }

//    @Test
    @DisplayName("Test to verify the response of wrap call")
    public void testWrapReactiveMethodCallReturnResultWithLongerContent() {
        String[] brokenWords = {"Develop a  ", "word wrap  ", "micro  ", "service.  ", "It should  ", "take an  ",
                "input  ", "string  ", "and  ", "return a  ", "wrapped  ", "string so  ", "that none  ", "of the  ",
                "lines are  ", "longer  ", "than the  ", "max  ", "length.  ", "The lines  ", "should  ", "not break  ",
                "any word  ", "in the  ", "middle. "};
        Boolean[] booleans = new Boolean[brokenWords.length + 1];
        for (int i = 0; i < brokenWords.length; i++) {
            booleans[i] = Boolean.TRUE;
        }
        booleans[booleans.length - 1] = Boolean.FALSE;

        when(mockAbstractContentWrapIterator.iterator()).thenReturn(mockAbstractContentWrapIterator);
        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(booleans[0], Arrays.copyOfRange(booleans, 1, booleans.length));
        when(mockAbstractContentWrapIterator.next()).thenReturn(brokenWords[0], Arrays.copyOfRange(brokenWords, 1, brokenWords.length));

        wordWrapService.reactive(CONTENT, DEFAULT_MAX_LENGTH).subscribe();

        verify(mockAbstractContentWrapIterator, times(1)).spliterator();
    }

    public static final String CONTENT = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
            "string so that none of the lines are longer than the max length. The lines should not break any word " +
            "in the middle. ";
}
