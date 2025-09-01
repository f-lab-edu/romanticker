package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CompositeTickerExtractorTest {

    @Test
    void extract_usesFirstPresent() {
        TickerExtractor a = mock(TickerExtractor.class);
        TickerExtractor b = mock(TickerExtractor.class);
        var comp = new CompositeTickerExtractor(List.of(a, b));

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(a.extract(eq(req), any())).thenReturn(Optional.empty());
        when(b.extract(eq(req), any())).thenReturn(Optional.of("TSLA"));

        assertThat(comp.extract(req, null)).contains("TSLA");
        verify(a).extract(eq(req), any());
        verify(b).extract(eq(req), any());
    }
}
