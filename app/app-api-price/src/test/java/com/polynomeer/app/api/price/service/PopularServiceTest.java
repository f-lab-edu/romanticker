package com.polynomeer.app.api.price.service;

import com.polynomeer.app.api.price.UsExchangeFilter;
import com.polynomeer.app.api.price.config.PopularItemAssembler;
import com.polynomeer.app.api.price.config.PopularQueryProperties;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import com.polynomeer.domain.popular.port.SymbolMetaPort;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PopularServiceTest {

    PopularCounterPort counter;
    SymbolMetaPort meta;
    PopularService service;

    @BeforeEach
    void setup() {
        counter = mock(PopularCounterPort.class);
        meta = mock(SymbolMetaPort.class);

        PopularQueryProperties props = new PopularQueryProperties();
        props.setDefaultExchange("US");
        props.setAllowedExchanges(Set.of("US", "NASDAQ", "NYSE", "AMEX"));
        props.setMaxLimit(10);

        PopularItemAssembler assembler = new PopularItemAssembler(new UsExchangeFilter(props), props);
        Clock fixed = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);

        service = new PopularService(counter, meta, assembler, props, fixed);
    }

    @Test
    void getPopular_capsLimit_andReturnsFilteredItems() {
        when(counter.topN(eq(PopularWindow.H1), eq(10), any())).thenReturn(List.of(new PopularCounterPort.RankItem("AAPL", 100), new PopularCounterPort.RankItem("BMW.DE", 70)));
        when(meta.findMeta(Set.of("AAPL", "BMW.DE"))).thenReturn(Map.of("AAPL", new SymbolMetaPort.SymbolMeta("AAPL", "Apple Inc.", "NASDAQ"), "BMW.DE", new SymbolMetaPort.SymbolMeta("BMW.DE", "BMW", "XETRA")));

        List<PopularQueryUseCase.PopularItem> out = service.getPopular(PopularWindow.H1, 99, "US");
        assertThat(out).extracting(PopularQueryUseCase.PopularItem::ticker).containsExactly("AAPL");

        verify(counter).topN(eq(PopularWindow.H1), eq(10), any()); // limit cap 10
        verify(meta).findMeta(eq(Set.of("AAPL", "BMW.DE")));
    }
}
