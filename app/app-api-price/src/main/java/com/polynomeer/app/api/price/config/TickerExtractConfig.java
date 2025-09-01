package com.polynomeer.app.api.price.config;

import com.polynomeer.app.api.price.service.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TickerExtractProperties.class)
public class TickerExtractConfig {

    @Bean
    public TickerExtractor tickerExtractor(TickerExtractProperties props) {
        return new CompositeTickerExtractor(List.of(new UriTemplateVariablesTickerExtractor(props.getPathVariableNames()), new QueryParamTickerExtractor(props.getQueryParamNames()), new HeaderTickerExtractor(props.getHeaderNames())));
    }
}
