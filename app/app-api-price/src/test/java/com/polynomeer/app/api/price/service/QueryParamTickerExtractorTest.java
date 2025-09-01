package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryParamTickerExtractorTest {

    @Test
    void extract_fromQueryParam() {
        var extractor = new QueryParamTickerExtractor(List.of("ticker", "symbol"));
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("ticker")).thenReturn("msft");

        assertThat(extractor.extract(req, null)).contains("MSFT");
    }

    @Test
    void extract_emptyWhenMissing() {
        var extractor = new QueryParamTickerExtractor(List.of("ticker"));
        HttpServletRequest req = mock(HttpServletRequest.class);

        assertThat(extractor.extract(req, null)).isEmpty();
    }
}
