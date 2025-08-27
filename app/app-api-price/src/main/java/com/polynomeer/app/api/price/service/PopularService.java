package com.polynomeer.app.api.price.service;

import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import com.polynomeer.domain.popular.port.SymbolMetaPort;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularService implements PopularQueryUseCase {

    private final PopularCounterPort counterPort;
    private final SymbolMetaPort metaPort;

    @Override
    public List<PopularItem> getPopular(PopularWindow window, int limit, String exchange) {
        int top = Math.min(10, Math.max(1, limit)); // 상한 10
        Instant nowUtc = Instant.now();

        var ranks = counterPort.topN(window, top, nowUtc);
        var tickers = ranks.stream().map(PopularCounterPort.RankItem::ticker).collect(Collectors.toSet());
        var metas = metaPort.findMeta(tickers);

        boolean usOnly = exchange == null || exchange.equalsIgnoreCase("US")
                || exchange.equalsIgnoreCase("NASDAQ")
                || exchange.equalsIgnoreCase("NYSE")
                || exchange.equalsIgnoreCase("AMEX");

        List<PopularItem> result = new ArrayList<>();
        for (var r : ranks) {
            var meta = metas.get(r.ticker());
            String ex = meta != null ? meta.exchange() : "US";
            if (usOnly && !isUsExchange(ex)) continue; // 해외주식만
            if (exchange != null && !exchange.equalsIgnoreCase("US")
                    && !exchange.equalsIgnoreCase(ex)) continue;

            result.add(new PopularItem(
                    r.ticker(),
                    meta != null ? meta.name() : "",
                    ex,
                    r.requests()
            ));
        }
        return result;
    }

    private boolean isUsExchange(String ex) {
        return "US".equalsIgnoreCase(ex)
                || "NASDAQ".equalsIgnoreCase(ex)
                || "NYSE".equalsIgnoreCase(ex)
                || "AMEX".equalsIgnoreCase(ex);
    }
}
