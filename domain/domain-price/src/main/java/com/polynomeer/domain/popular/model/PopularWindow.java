package com.polynomeer.domain.popular.model;

import java.time.Duration;

public enum PopularWindow {
    M15(Duration.ofMinutes(15)),
    H1(Duration.ofHours(1)),
    D1(Duration.ofDays(1)),
    D7(Duration.ofDays(7));

    public final Duration length;

    PopularWindow(Duration length) {
        this.length = length;
    }

    public static PopularWindow fromQuery(String q) {
        return switch (q == null ? "" : q.toLowerCase()) {
            case "15m" -> M15;
            case "1h" -> H1;
            case "24h", "1d" -> D1;
            case "7d" -> D7;
            default -> H1;
        };
    }

    public Duration length() {
        return length;
    }
}
