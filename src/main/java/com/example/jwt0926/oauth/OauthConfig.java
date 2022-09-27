package com.example.jwt0926.oauth;

import com.example.jwt0926.dto.provider.InMemoryProviderRepository;
import com.example.jwt0926.dto.provider.OauthProperties;
import com.example.jwt0926.dto.provider.OauthProvider;
import com.example.jwt0926.oauth.OauthAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {
    private final OauthProperties properties;

    public OauthConfig(OauthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);

        return new InMemoryProviderRepository(providers);
    }
}
