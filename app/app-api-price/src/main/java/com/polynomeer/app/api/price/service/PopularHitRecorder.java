package com.polynomeer.app.api.price.service;

import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class PopularHitRecorder {

    private final PopularCounterPort counter;

    private final Set<PopularWindow> windows;

    public PopularHitRecorder(
            PopularCounterPort counter,
            @Value("${app.popular.count.windows:M15,H1,D1,D7}") String windowsProp
    ) {
        this.counter = counter;
        this.windows = parse(windowsProp);
    }

    public void recordHit(String ticker, Instant nowUtc) {
        counter.incrementBatch(ticker, windows, nowUtc);
    }

    private static Set<PopularWindow> parse(String prop) {
        if (prop == null || prop.isBlank())
            return EnumSet.of(PopularWindow.M15, PopularWindow.H1, PopularWindow.D1, PopularWindow.D7);
        List<PopularWindow> list = Stream.of(prop.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(PopularWindow::valueOf)
                .toList();
        return EnumSet.copyOf(list);
    }
}
