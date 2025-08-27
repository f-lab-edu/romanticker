package com.polynomeer.app.api.price.controller;

import com.polynomeer.domain.popular.model.PopularItemDto;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PopularController {

    private final PopularQueryUseCase useCase;

    @GetMapping("/popular")
    public List<PopularItemDto> popular(
            @RequestParam(defaultValue = "H1") String window,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "US") String exchange
    ) {
        PopularWindow w = PopularWindow.fromQuery(window); // 도메인 enum에 fromQuery 제공
        return useCase.getPopular(w, limit, exchange).stream()
                .map(i -> new PopularItemDto(i.ticker(), i.name(), i.exchange(), i.requests()))
                .toList();
    }
}
