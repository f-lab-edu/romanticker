package com.polynomeer.app.api.price.service;

import com.polynomeer.app.api.price.config.PopularCountProperties;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularHitRecorder {

    private final PopularCounterPort counter;
    private final PopularCountProperties props;

    public void recordHit(String ticker, Instant nowUtc) {
        Set<PopularWindow> windows = props.getWindows();
        counter.increment(ticker, windows, nowUtc);
    }
}
