package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface TickerExtractor {
    Optional<String> extract(HttpServletRequest request, @Nullable Object handler);
}
