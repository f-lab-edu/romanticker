package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeaderTickerExtractorTest {

    @Test
    void extract_fromHeader() {
        var extractor = new HeaderTickerExtractor(List.of("X-Ticker"));
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("X-Ticker")).thenReturn("amzn");

        assertThat(extractor.extract(req, null)).contains("AMZN");
    }

    @Test
    void extract_emptyWhenMissing() {
        var extractor = new HeaderTickerExtractor(List.of("X-Ticker"));
        HttpServletRequest req = mock(HttpServletRequest.class);

        assertThat(extractor.extract(req, null)).isEmpty();
    }
}
