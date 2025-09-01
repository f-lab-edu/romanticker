package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public class CompositeTickerExtractor implements TickerExtractor {
    private final List<TickerExtractor> delegates;

    public CompositeTickerExtractor(List<TickerExtractor> delegates) {
        this.delegates = List.copyOf(delegates);
    }

    @Override
    public Optional<String> extract(HttpServletRequest request, @Nullable Object handler) {
        for (TickerExtractor d : delegates) {
            Optional<String> t = d.extract(request, handler);
            if (t.isPresent()) return t;
        }
        return Optional.empty();
    }
}
