package com.naveen.microservice.wordwrap.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiKeyRepositoryImplUnitTest {
    @Test
    public void testWithInvalidSimulatedKeys() {
        ApiKeyRepository keys = new ApiKeyRepositoryImpl(null);
        assertFalse(keys.isValidApiKey("aa"));

        keys = new ApiKeyRepositoryImpl("aa");
        assertTrue(keys.isValidApiKey("aa"));

        keys = new ApiKeyRepositoryImpl("");
        assertFalse(keys.isValidApiKey("fgdert643tdfg"));

        assertFalse(keys.isValidApiKey(null));
    }

    @Test
    public void testWithValidSimulatedKeysList() {
        String simulatedKeys = "13fcfad2-aaf3-11eb-bcbc-0242ac130002," +
                "13fcfcc6-aaf3-11eb-bcbc-0242ac130002," +
                "13fcfdac-aaf3-11eb-bcbc-0242ac130002," +
                "13fcfe6a-aaf3-11eb-bcbc-0242ac130002," +
                "13fcff28-aaf3-11eb-bcbc-0242ac130002";

        ApiKeyRepository keys = new ApiKeyRepositoryImpl(simulatedKeys);
        assertTrue(keys.isValidApiKey("13fcfe6a-aaf3-11eb-bcbc-0242ac130002"));
        assertFalse(keys.isValidApiKey("13sd2e6a-aaf3-11eb-bcbc-0242ac130002"));
    }

    @Test
    public void testWithValidSimulatedKey() {
        String simulatedKeys = "13fcfad2-aaf3-11eb-bcbc-0242ac130002";

        ApiKeyRepository keys = new ApiKeyRepositoryImpl(simulatedKeys);
        assertTrue(keys.isValidApiKey("13fcfad2-aaf3-11eb-bcbc-0242ac130002"));
        assertFalse(keys.isValidApiKey("13sd2e6a-aaf3-11eb-bcbc-0242ac130002"));
    }
}
