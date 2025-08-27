package com.polynomeer.app.api.price.controller;

import com.polynomeer.app.api.price.service.PopularService;
import com.polynomeer.domain.popular.model.PopularItemDto;
import com.polynomeer.domain.popular.model.PopularWindow;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PopularController {
    private final PopularService service;

    @GetMapping("/popular")
    public List<PopularItemDto> popular(
            @RequestParam(name = "window", required = false, defaultValue = "1h") String window,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "exchange", required = false, defaultValue = "US") String exchange
    ) {
        // 해외주식 전용 강제
        if (!"US".equalsIgnoreCase(exchange) && !"NASDAQ".equalsIgnoreCase(exchange)
                && !"NYSE".equalsIgnoreCase(exchange) && !"AMEX".equalsIgnoreCase(exchange)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only foreign (US) symbols are supported.");
        }
        return service.getPopular(PopularWindow.valueOf(window), limit, exchange).stream().map(item -> new PopularItemDto(item)).toList();
    }
}