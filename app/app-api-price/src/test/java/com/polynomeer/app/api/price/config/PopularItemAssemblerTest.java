package com.polynomeer.app.api.price.config;

import com.polynomeer.app.api.price.UsExchangeFilter;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import com.polynomeer.domain.popular.port.SymbolMetaPort;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PopularItemAssemblerTest {

    @Test
    void assemble_filtersAndMaps() {
        PopularQueryProperties props = new PopularQueryProperties();
        props.setAllowedExchanges(Set.of("US", "NASDAQ", "NYSE", "AMEX"));
        props.setDefaultExchange("US");

        var assembler = new PopularItemAssembler(new UsExchangeFilter(props), props);

        List<PopularCounterPort.RankItem> ranks = List.of(
                new PopularCounterPort.RankItem("AAPL", 100),
                new PopularCounterPort.RankItem("SIE.DE", 50)
        );

        Map<String, SymbolMetaPort.SymbolMeta> metas = Map.of(
                "AAPL", new SymbolMetaPort.SymbolMeta("AAPL", "Apple Inc.", "NASDAQ"),
                "SIE.DE", new SymbolMetaPort.SymbolMeta("SIE.DE", "Siemens AG", "XETRA")
        );

        List<PopularQueryUseCase.PopularItem> out = assembler.assemble(ranks, metas, "US");

        assertThat(out).hasSize(1);
        assertThat(out.getFirst().ticker()).isEqualTo("AAPL");
        assertThat(out.getFirst().exchange()).isEqualTo("NASDAQ");
        assertThat(out.getFirst().requests()).isEqualTo(100);
    }
}
