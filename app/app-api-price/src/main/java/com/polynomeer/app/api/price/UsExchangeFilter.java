package com.polynomeer.app.api.price;

import com.polynomeer.app.api.price.config.PopularQueryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UsExchangeFilter implements ExchangeFilter {

    private final PopularQueryProperties props;

    @Override
    public boolean isAllowed(String symbolExchange, String requested) {
        String ex = (symbolExchange == null || symbolExchange.isBlank())
                ? props.getDefaultExchange()
                : symbolExchange.toUpperCase(Locale.US);

        if (requested == null || requested.equalsIgnoreCase("US")) {
            return props.getAllowedExchanges().contains(ex);
        }
        return ex.equalsIgnoreCase(requested) && props.getAllowedExchanges().contains(ex);
    }
}
