package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class QueryParamTickerExtractor implements TickerExtractor {
    private final List<String> paramNames;

    public QueryParamTickerExtractor(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    @Override
    public Optional<String> extract(HttpServletRequest request, @Nullable Object handler) {
        for (String p : paramNames) {
            String v = request.getParameter(p);
            if (v != null && !v.isBlank()) {
                return Optional.of(v.toUpperCase(Locale.US));
            }
        }
        return Optional.empty();
    }
}
