package com.polynomeer.app.api.price;

import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PopularCountingInterceptor implements HandlerInterceptor {

    private final PopularCounterPort counter;

    @Override
    public void afterCompletion(@NotNull HttpServletRequest req, @NotNull HttpServletResponse res, @NotNull Object handler, Exception ex) {
        if (!(handler instanceof HandlerMethod)) return;
        if (res.getStatus() < 200 || res.getStatus() >= 300) return;

        String uri = req.getRequestURI();
        String ticker = extractTickerFromUri(uri);
        if (ticker == null) return;

        Instant now = Instant.now();
        // 여러 윈도우에 동시 증가
        counter.increment(ticker, PopularWindow.M15, now);
        counter.increment(ticker, PopularWindow.H1, now);
        counter.increment(ticker, PopularWindow.D1, now);
        counter.increment(ticker, PopularWindow.D7, now);
    }

    private String extractTickerFromUri(String uri) {
        // /api/v1/quotes/{ticker} 또는 /api/v1/charts/{ticker}
        String[] p = uri.split("/");
        if (p.length >= 5) {
            String r = p[3];
            if ("quotes".equals(r) || "charts".equals(r)) {
                return p[4].toUpperCase(Locale.US);
            }
        }
        return null;
    }
}
