package com.polynomeer.app.api.price.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Set;

@Getter
@ConfigurationProperties(prefix = "app.popular.query")
public class PopularQueryProperties {
    @Setter
    private int maxLimit = 10;
    private String defaultExchange = "US";
    @Setter
    private Set<String> allowedExchanges = Set.of("US", "NASDAQ", "NYSE", "AMEX");

    public void setDefaultExchange(String defaultExchange) {
        this.defaultExchange = defaultExchange == null ? "US" : defaultExchange.toUpperCase(Locale.US);
    }
}
