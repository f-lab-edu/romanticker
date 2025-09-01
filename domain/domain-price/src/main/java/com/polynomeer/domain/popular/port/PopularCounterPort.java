package com.polynomeer.domain.popular.port;

import com.polynomeer.domain.popular.model.PopularWindow;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface PopularCounterPort {
    void increment(String ticker, PopularWindow window, Instant atUtc);

    void increment(String ticker, Collection<PopularWindow> windows, Instant atUtc);

    List<RankItem> topN(PopularWindow window, int limit, Instant nowUtc);

    record RankItem(String ticker, long requests) {
    }
}
