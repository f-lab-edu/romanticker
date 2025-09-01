package com.polynomeer.app.api.price;

import com.polynomeer.app.api.price.config.PopularQueryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UsExchangeFilterTest {

    private UsExchangeFilter filter;

    @BeforeEach
    void setUp() {
        PopularQueryProperties props = new PopularQueryProperties();
        props.setDefaultExchange("US");
        props.setAllowedExchanges(Set.of("US", "NASDAQ", "NYSE", "AMEX"));
        filter = new UsExchangeFilter(props);
    }

    @Test
    void allowed_whenRequestedUS_andMetaNASDAQ() {
        assertThat(filter.isAllowed("NASDAQ", "US")).isTrue();
    }

    @Test
    void allowed_whenRequestedSpecificExchange_matches() {
        assertThat(filter.isAllowed("NYSE", "NYSE")).isTrue();
        assertThat(filter.isAllowed("NASDAQ", "NYSE")).isFalse();
    }

    @Test
    void defaultExchange_whenMetaMissing() {
        assertThat(filter.isAllowed(null, "US")).isTrue(); // default US
        assertThat(filter.isAllowed("", "NASDAQ")).isFalse();
    }
}
