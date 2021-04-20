package com.naveen.microservice.wordwrap.wrap;


import com.naveen.microservice.wordwrap.wrap.model.Content;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractContentWrapIteratorTest {

    @Test
    public void test() {
        final String content = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
                "string so that none of the lines are longer than the max length. The lines should not break any word " +
                "in the middle. ";
        final int expectedNumberOfIterations = 25;
        int maxLength = 10;
        callIterator(expectedNumberOfIterations, content, maxLength);
    }

    @Test
    public void testx() {
        final String content = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
                "string so that none of the lines are longer than the max length. The lines should not break any word " +
                "in the middle. ";
        final int expectedNumberOfIterations = 39;
        int maxLength = 3;
        callIterator(expectedNumberOfIterations, content, maxLength);
    }

    @Test
    public void test2() {
        final int maxLength = 23;
        final String content = "Develop a word wrap micro service. It should take an input string and return a wrapped " +
                "string so that none of the lines are longer than the max length. The lines should not break any word " +
                "in the middle. ";
        final int expectedNumberOfIterations = 10;

        callIterator(expectedNumberOfIterations, content, maxLength);
    }

    @Test
    public void test3() {
        final int maxLength = 23;
        final String content = "NAVEEN";
        final int expectedNumberOfIterations = 1;

        callIterator(expectedNumberOfIterations, content, maxLength);
    }

    @Test
    public void test4() {
        final int maxLength = 23;
        final String content = "";
        final int expectedNumberOfIterations = 1;

        callIterator(expectedNumberOfIterations, content, maxLength);
    }

    @Test
    public void test5() {
        final int maxLength = 23;
        final int expectedNumberOfIterations = 0;

        assertThrows(IllegalArgumentException.class, () -> {
            callIterator(expectedNumberOfIterations, null, maxLength);
        });
    }

    private void callIterator(int expectedNumberOfIterations, final String content, int maxLength) {
        Content build = Content.builder().content(content).build();

        int iterationCount = 0;

        AbstractContentWrapIterator abstractContentWrapIterator = new InMemoryContentWrapper(build, maxLength);

        while (abstractContentWrapIterator.hasNext()) {
            iterationCount += 1;
            final String line = abstractContentWrapIterator.next();
            System.out.println(line);
        }

        assertEquals(expectedNumberOfIterations, iterationCount);
    }
}
