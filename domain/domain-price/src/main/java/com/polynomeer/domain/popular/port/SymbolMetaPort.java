package com.polynomeer.domain.popular.port;

import java.util.Map;
import java.util.Set;

public interface SymbolMetaPort {
    /**
     * tickers에 대한 메타정보를 반환한다.
     * key=ticker, value=SymbolMeta(이름/거래소 등)
     */
    Map<String, SymbolMeta> findMeta(Set<String> tickers);

    record SymbolMeta(String ticker, String name, String exchange) {
    }
}
