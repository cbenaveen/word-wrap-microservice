package com.naveen.microservice.wordwrap.repository;

public interface ApiKeyRepository {
    boolean isValidApiKey(final String apiKey);
}
