package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UriTemplateVariablesTickerExtractorTest {

    @Test
    void extract_fromPathVariables() {
        var extractor = new UriTemplateVariablesTickerExtractor(Set.of("ticker", "symbol"));
        HttpServletRequest req = mock(HttpServletRequest.class);

        when(req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .thenReturn(Map.of("ticker", "aapl"));

        Optional<String> t = extractor.extract(req, null);
        assertThat(t).contains("AAPL");
    }

    @Test
    void extract_emptyWhenNoVar() {
        var extractor = new UriTemplateVariablesTickerExtractor(Set.of("ticker"));
        HttpServletRequest req = mock(HttpServletRequest.class);

        when(req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .thenReturn(Map.of());

        assertThat(extractor.extract(req, null)).isEmpty();
    }
}
