package com.polynomeer.app.api.price.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(PopularQueryProperties.class)
public class PopularQueryConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
