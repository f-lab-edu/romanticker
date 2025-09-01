package com.polynomeer.app.api.price;

import com.polynomeer.app.api.price.service.PopularHitRecorder;
import com.polynomeer.app.api.price.service.TickerExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PopularCountingInterceptorTest {

    static class DummyHandler {
        public void h() {
        }
    }

    @Test
    void afterCompletion_success2xx_recordsHit() throws Exception {
        PopularHitRecorder recorder = mock(PopularHitRecorder.class);
        TickerExtractor extractor = mock(TickerExtractor.class);
        PopularCountingInterceptor itc = new PopularCountingInterceptor(recorder, extractor);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(res.getStatus()).thenReturn(200);

        when(extractor.extract(eq(req), any())).thenReturn(Optional.of("AAPL"));

        Method m = DummyHandler.class.getMethod("h");
        HandlerMethod handler = new HandlerMethod(new DummyHandler(), m);

        itc.afterCompletion(req, res, handler, null);

        verify(recorder, times(1)).recordHit(eq("AAPL"), any(Instant.class));
    }

    @Test
    void afterCompletion_non2xx_doesNothing() throws Exception {
        PopularHitRecorder recorder = mock(PopularHitRecorder.class);
        TickerExtractor extractor = mock(TickerExtractor.class);
        PopularCountingInterceptor itc = new PopularCountingInterceptor(recorder, extractor);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(res.getStatus()).thenReturn(404);

        Method m = DummyHandler.class.getMethod("h");
        HandlerMethod handler = new HandlerMethod(new DummyHandler(), m);

        itc.afterCompletion(req, res, handler, null);

        verifyNoInteractions(recorder);
        verifyNoInteractions(extractor);
    }
}
