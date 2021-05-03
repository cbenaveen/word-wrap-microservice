package com.naveen.microservice.wordwrap.wrap;

import com.naveen.microservice.wordwrap.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharPositionBasedContentWrapperUnitTest {
    public static final String CONTENT = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
            "string so that none of the lines are longer than the max length. The lines should not break any word " +
            "in the middle. ";

    @ParameterizedTest
    @DisplayName("Test to verify the iterable functional by passing a valid content, max length, and position and verify the result")
    @ArgumentsSource(TestArgumentsProvider.class)
    public void testContentWrapWithDifferentContentsAndMaxLengthAndCharPosition(final String content, int maxLength, int currentCharIndex, int expectedNumberOfIterations) {
        Content build = Content.builder().content(content).build();

        AbstractContentWrapIterator abstractContentWrapIterator = new CharPositionBasedContentWrapper(build,
                maxLength, currentCharIndex);

        int iterationCount = 0;
        for(String line: abstractContentWrapIterator) {
            iterationCount += 1;
        }

        assertEquals(expectedNumberOfIterations, iterationCount);
    }

    private static class TestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            /**
             * The arguments are passed in the order of:
             * 1) CONTENT, 2) Max length 3) current char index 4) expected number of lines
             */
            return Stream.of(
                    Arguments.of(CONTENT, 10, -1, 0),
                    Arguments.of(CONTENT, 3, 0, 39),
                    Arguments.of(CONTENT, 23, 0, 11),
                    Arguments.of("NAVEEN", 23, 0, 1),
                    Arguments.of("NAVEEN", 23, 7, 0),
                    Arguments.of("", 23, 0, 0),
                    Arguments.of(" ", 7, 0, 1)
            );
        }
    }
}
