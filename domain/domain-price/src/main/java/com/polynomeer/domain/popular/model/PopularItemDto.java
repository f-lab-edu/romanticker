package com.polynomeer.domain.popular.model;

import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;

public record PopularItemDto(
        String ticker,
        String name,     // optional
        String exchange, // e.g., NASDAQ
        long requests    // 스코어(요청수)
) {

    public PopularItemDto(PopularQueryUseCase.PopularItem item) {
        this(item.ticker(), item.name(), item.exchange(), item.requests());
    }
}
