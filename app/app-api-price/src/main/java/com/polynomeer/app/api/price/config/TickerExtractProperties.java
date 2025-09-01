package com.polynomeer.app.api.price.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.ticker.extract")
public class TickerExtractProperties {
    // URI 템플릿 변수 이름 후보: {ticker}, {symbol} 등
    private Set<String> pathVariableNames = Set.of("ticker", "symbol");
    // 쿼리파라미터 후보: ?ticker=, ?symbol=
    private List<String> queryParamNames = List.of("ticker", "symbol");
    // 헤더 후보: X-Ticker 등
    private List<String> headerNames = List.of("X-Ticker");

}

