package com.example.jwt0926.dto.provider;

import java.util.HashMap;
import java.util.Map;

// memory에서 provider 찾기
// ex. github
public class InMemoryProviderRepository {
    private final Map<String, OauthProvider> providers;

    public InMemoryProviderRepository(Map<String, OauthProvider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OauthProvider findByProviderName(String name) {
        return providers.get(name);
    }
}
