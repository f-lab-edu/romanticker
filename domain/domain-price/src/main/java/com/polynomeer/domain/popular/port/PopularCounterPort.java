package com.polynomeer.domain.popular.port;

import com.polynomeer.domain.popular.model.PopularWindow;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface PopularCounterPort {
    void increment(String ticker, PopularWindow window, Instant atUtc);

    default void incrementBatch(String ticker, Collection<PopularWindow> windows, Instant atUtc) {
        if (windows == null || windows.isEmpty()) return;
        for (PopularWindow w : windows) {
            increment(ticker, w, atUtc);
        }
    }

    List<RankItem> topN(PopularWindow window, int limit, Instant nowUtc);

    record RankItem(String ticker, long requests) {
    }
}
