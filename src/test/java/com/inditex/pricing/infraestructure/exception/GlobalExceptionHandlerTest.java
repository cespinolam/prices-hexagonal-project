package com.inditex.pricing.infraestructure.exception;

import com.inditex.pricing.domain.port.in.PriceUseCase;
import com.inditex.pricing.infrastructure.adapter.out.persistence.mapper.PriceMapper;
import com.inditex.pricing.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceUseCase priceUseCase;

    @MockBean
    private PriceMapper priceMapper;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();


    @Test
    void testHandleTypeMismatch() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("brandId", "invalid")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid parameter")));
    }

    @Test
    void testHandleGeneralError() throws Exception {
        Mockito.when(priceUseCase.getApplicablePrice(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new RuntimeException("General error"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("An unexpected error occurred")));
    }
}
