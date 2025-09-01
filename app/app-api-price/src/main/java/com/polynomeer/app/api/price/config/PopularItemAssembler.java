package com.polynomeer.app.api.price.config;

import com.polynomeer.app.api.price.ExchangeFilter;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import com.polynomeer.domain.popular.port.SymbolMetaPort;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PopularItemAssembler {

    private final ExchangeFilter exchangeFilter;
    private final PopularQueryProperties props;

    public List<PopularQueryUseCase.PopularItem> assemble(
            List<PopularCounterPort.RankItem> ranks,
            Map<String, SymbolMetaPort.SymbolMeta> metas,
            String requestedExchange
    ) {
        List<PopularQueryUseCase.PopularItem> out = new ArrayList<>(ranks.size());
        for (var r : ranks) {
            var meta = metas.get(r.ticker());
            String name = (meta == null || meta.name() == null) ? "" : meta.name();
            String ex = (meta == null || meta.exchange() == null || meta.exchange().isBlank())
                    ? props.getDefaultExchange()
                    : meta.exchange();

            if (!exchangeFilter.isAllowed(ex, requestedExchange)) continue;

            out.add(new PopularQueryUseCase.PopularItem(
                    r.ticker(), name, ex, r.requests()
            ));
        }
        return out;
    }
}
