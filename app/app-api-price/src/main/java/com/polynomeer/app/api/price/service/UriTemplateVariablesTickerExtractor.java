package com.polynomeer.app.api.price.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UriTemplateVariablesTickerExtractor implements TickerExtractor {

    private final Set<String> candidateNames;

    public UriTemplateVariablesTickerExtractor(Set<String> candidateNames) {
        this.candidateNames = candidateNames;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<String> extract(HttpServletRequest request, @Nullable Object handler) {
        Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (!(attr instanceof Map<?, ?> map)) return Optional.empty();
        Map<String, String> vars = (Map<String, String>) map;
        for (String key : candidateNames) {
            String v = vars.get(key);
            if (v != null && !v.isBlank()) {
                return Optional.of(v.toUpperCase(Locale.US));
            }
        }
        return Optional.empty();
    }
}
