package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsUnitTest {

    @Test
    @DisplayName("Test to verify the Content POJO creation using the Utils method by passing null content")
    public void testContentObjectCreationWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Utils.getContent(null);
        });
    }

    @Test
    @DisplayName("Test to verify the Content POJO creation using the Utils method")
    public void testContentObjectCreation() {
        final String c = "NAVEEN";
        Content content = Utils.getContent(c);
        assertEquals(c, content.getContent());
    }

}
