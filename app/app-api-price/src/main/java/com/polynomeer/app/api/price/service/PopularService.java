package com.polynomeer.app.api.price.service;

import com.polynomeer.app.api.price.config.PopularItemAssembler;
import com.polynomeer.app.api.price.config.PopularQueryProperties;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import com.polynomeer.domain.popular.port.SymbolMetaPort;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularService implements PopularQueryUseCase {

    private final PopularCounterPort counterPort;
    private final SymbolMetaPort metaPort;
    private final PopularItemAssembler assembler;
    private final PopularQueryProperties props;
    private final Clock clock;

    @Override
    public List<PopularItem> getPopular(PopularWindow window, int limit, String exchange) {
        int top = Math.min(props.getMaxLimit(), Math.max(1, limit));
        Instant nowUtc = Instant.now(clock);

        var ranks = counterPort.topN(window, top, nowUtc);
        var tickers = ranks.stream().map(PopularCounterPort.RankItem::ticker).collect(Collectors.toSet());
        var metas = metaPort.findMeta(tickers);

        return assembler.assemble(ranks, metas, exchange);
    }
}
