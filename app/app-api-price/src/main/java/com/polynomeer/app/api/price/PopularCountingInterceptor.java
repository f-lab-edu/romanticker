package com.polynomeer.app.api.price;

import com.polynomeer.app.api.price.service.PopularHitRecorder;
import com.polynomeer.app.api.price.service.TickerExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class PopularCountingInterceptor implements HandlerInterceptor {

    private final PopularHitRecorder recorder;
    private final TickerExtractor tickerExtractor;

    @Override
    public void afterCompletion(@NotNull HttpServletRequest req, @NotNull HttpServletResponse res, @NotNull Object handler, Exception ex) {
        if (!(handler instanceof HandlerMethod)) return;
        if (res.getStatus() < 200 || res.getStatus() >= 300) return;

        tickerExtractor.extract(req, handler).ifPresent(ticker ->
                recorder.recordHit(ticker, Instant.now())
        );
    }
}