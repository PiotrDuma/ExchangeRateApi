package com.github.PiotrDuma.ExchangeRateApi.domain;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExchangeController.class)
class ExchangeControllerTest {

  @MockitoBean
  private ExchangeService service;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  private CurrencyType base = CurrencyType.EUR;

  @Test
  void postMethodShouldInvokeService() throws Exception {
    mockMvc.perform(post("/exchange")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(getRequestDTO())))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().stringValues("Location", is(base)));

    verify(this.service, times(1)).create(any());
  }

  private ExchangeRateRequestDTO getRequestDTO() {
    Set<CurrencyType> rates = new LinkedHashSet<>(List.of(CurrencyType.PLN));
    return new ExchangeRateRequestDTO(base, rates);
  }
}