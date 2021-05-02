package com.naveen.microservice.wordwrap.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

@Repository
class ApiKeyRepositoryImpl implements ApiKeyRepository {
    private final Set<String> apiKeys;

    ApiKeyRepositoryImpl(@Value("${simulated.api.keys}") final String simulatedApiKeys) {
        if (Objects.nonNull(simulatedApiKeys)) {
            String[] splitKeys = simulatedApiKeys.split(",");
            if (splitKeys != null && splitKeys.length > 0) {
                apiKeys = Set.of(splitKeys);
            } else {
                apiKeys = Set.of();
            }
        } else {
            apiKeys = Set.of();
        }
    }

    @Override
    public boolean isValidApiKey(String apiKey) {
        if (Objects.isNull(apiKey)) return false;
        return apiKeys.contains(apiKey);
    }
}
