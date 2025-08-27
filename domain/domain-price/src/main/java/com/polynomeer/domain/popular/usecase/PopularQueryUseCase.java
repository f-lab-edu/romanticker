package com.polynomeer.domain.popular.usecase;

import com.polynomeer.domain.popular.model.PopularWindow;

import java.util.List;

public interface PopularQueryUseCase {
    List<PopularItem> getPopular(PopularWindow window, int limit, String exchange);

    record PopularItem(String ticker, String name, String exchange, long requests) {
    }
}
