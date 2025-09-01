package com.polynomeer.infra.redis;

import com.polynomeer.domain.popular.port.SymbolMetaPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class DbOrCacheSymbolMetaAdapter implements SymbolMetaPort {
    @Override
    public Map<String, SymbolMetaPort.SymbolMeta> findMeta(Set<String> tickers) {
        // TODO: DB/캐시 조회로 대체
        Map<String, SymbolMeta> map = new HashMap<>();
        for (String t : tickers) {
            map.put(t, new SymbolMeta(t, "", "US"));
        }
        return map;
    }
}
