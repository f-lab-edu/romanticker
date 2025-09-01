package com.polynomeer.app.api.price.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.usecase.PopularQueryUseCase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PopularController.class)
class PopularControllerTest {

    @Resource
    MockMvc mvc;

    @MockitoBean
    PopularQueryUseCase useCase;

    @Resource
    ObjectMapper om;

    @Test
    void popular_endpoint_mapsParamsAndReturnsDtos() throws Exception {
        Mockito.when(useCase.getPopular(eq(PopularWindow.H1), eq(10), eq("US")))
                .thenReturn(List.of(
                        new PopularQueryUseCase.PopularItem("AAPL", "Apple Inc.", "NASDAQ", 100)
                ));

        mvc.perform(get("/api/v1/popular")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ticker").value("AAPL"))
                .andExpect(jsonPath("$[0].exchange").value("NASDAQ"))
                .andExpect(jsonPath("$[0].requests").value(100));
    }
}
