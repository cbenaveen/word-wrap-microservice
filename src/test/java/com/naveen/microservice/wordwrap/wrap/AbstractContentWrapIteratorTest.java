package com.naveen.microservice.wordwrap.wrap;


import com.naveen.microservice.wordwrap.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractContentWrapIteratorTest {

    public static final String CONTENT = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
            "string so that none of the lines are longer than the max length. The lines should not break any word " +
            "in the middle. ";

    @ParameterizedTest
    @DisplayName("Test to verify the iterable functional by passing a valid content, max length and verify the result")
    @ArgumentsSource(TestArgumentsProvider.class)
    public void testIterable(final String content, final int maxLength, final int expectedNumberOfIterations) {
        performIteration(content, maxLength, expectedNumberOfIterations);
    }

    @ParameterizedTest
    @DisplayName("Test to verify the iterator functional by passing a valid content, max length and verify the result")
    @ArgumentsSource(TestArgumentsProvider.class)
    public void testIterator(final String content, final int maxLength, final int expectedNumberOfIterations) {
        iterate(content, maxLength, expectedNumberOfIterations);
    }

    @Test
    @DisplayName("Test to verify the Content builder when passing null as content both explicitly and implicit")
    public void testContentBuilderWithNullContent() {
        // explicitly feed null value to builder for the content
        assertThrows(IllegalArgumentException.class, () -> {
            Content.builder().content(null).build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Content.builder().build();
        });
    }

    @Test
    @DisplayName("Test to verify iterator behaviour when null Content object is passed in")
    public void testIteratorWithNullContentObject() {
        assertThrows(IllegalArgumentException.class, () -> {
            AbstractContentWrapIterator abstractContentWrapIterator = new WordByWordSplitWrapper(null, 10);
            while (abstractContentWrapIterator.hasNext()) {
                abstractContentWrapIterator.next();
            }
        });
    }

    @Test
    @DisplayName("Test to verify iterator remove method throws exception as the method is not supported")
    public void testIteratorRemove() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Content content = Content.builder().content(CONTENT).build();
            AbstractContentWrapIterator abstractContentWrapIterator = new WordByWordSplitWrapper(content,
                    10);
            abstractContentWrapIterator.remove();
        });
    }

    @ParameterizedTest
    @DisplayName("Test to verify iterator behaviour when max length is set with a value lesser than 1")
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    public void testIteratorWithInvalidMaxLength(int invalidMaxLength) {
        assertThrows(IllegalArgumentException.class, () -> {
            Content content = Content.builder().content(CONTENT).build();
            AbstractContentWrapIterator abstractContentWrapIterator = new WordByWordSplitWrapper(content,
                    invalidMaxLength);
            while (abstractContentWrapIterator.hasNext()) {
                abstractContentWrapIterator.next();
            }
        });
    }

    private void performIteration(final String content, int maxLength, int expectedNumberOfIterations) {
        Content build = Content.builder().content(content).build();

        int iterationCount = 0;

        AbstractContentWrapIterator abstractContentWrapIterator = new WordByWordSplitWrapper(build, maxLength);

        while (abstractContentWrapIterator.hasNext()) {
            iterationCount += 1;
            abstractContentWrapIterator.next();
        }

        assertEquals(expectedNumberOfIterations, iterationCount);
    }

    private void iterate(final String content, int maxLength, int expectedNumberOfIterations) {
        Content build = Content.builder().content(content).build();
        int iterationCount = 0;
        for(String line: new WordByWordSplitWrapper(build, maxLength)) {
            iterationCount += 1;
        }

        assertEquals(expectedNumberOfIterations, iterationCount);
    }

    private static class TestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            /**
             * The arguments are passed in the order of: 1) CONTENT, 2) Max length 3) expected iteration of result
             */
            return Stream.of(
                    Arguments.of(CONTENT, 10, 25),
                    Arguments.of(CONTENT, 3, 40),
                    Arguments.of(CONTENT, 23, 10),
                    Arguments.of("NAVEEN", 23, 1),
                    Arguments.of("      Develop a   word      ", 5, 7),
                    Arguments.of("    ", 23, 1),
                    Arguments.of("", 23, 1)
            );
        }
    }
}
