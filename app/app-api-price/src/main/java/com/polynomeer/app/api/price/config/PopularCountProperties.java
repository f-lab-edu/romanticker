package com.polynomeer.app.api.price.config;

import com.polynomeer.domain.popular.model.PopularWindow;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumSet;
import java.util.Set;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.popular.count")
public class PopularCountProperties {
    private Set<PopularWindow> windows = EnumSet.of(
            PopularWindow.M15, PopularWindow.H1, PopularWindow.D1, PopularWindow.D7
    );
}
