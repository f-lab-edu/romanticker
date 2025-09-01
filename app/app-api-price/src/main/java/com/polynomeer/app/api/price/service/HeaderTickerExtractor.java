package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class HeaderTickerExtractor implements TickerExtractor {
    private final List<String> headerNames;

    public HeaderTickerExtractor(List<String> headerNames) {
        this.headerNames = headerNames;
    }

    @Override
    public Optional<String> extract(HttpServletRequest request, @Nullable Object handler) {
        for (String h : headerNames) {
            String v = request.getHeader(h);
            if (v != null && !v.isBlank()) {
                return Optional.of(v.toUpperCase(Locale.US));
            }
        }
        return Optional.empty();
    }
}
