package com.github.PiotrDuma.ExchangeRateApi.domain;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.config.FixedClockConfig;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.time.Clock;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExchangeController.class)
@Import(FixedClockConfig.class)
class ExchangeControllerTest {
  private static final Set<CurrencyType> CURRENCY_TYPES = new HashSet<>(List.of(CurrencyType.USD));
  @MockitoBean
  private ExchangeService service;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Clock clock;
  @Autowired
  private ObjectMapper objectMapper;

  private CurrencyType base = CurrencyType.EUR;

  @Test
  void postMethodShouldInvokeService() throws Exception {
    when(this.service.create(any())).thenReturn(exchangeRateResponseDTO());

    mockMvc.perform(post("/api/exchange")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(getRequestDTO())))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", containsString("/api/exchange/" + base)));

    verify(this.service, times(1)).create(any());
  }

  private ExchangeRateResponseDTO exchangeRateResponseDTO(){
    return new ExchangeRate(CurrencyType.EUR, CURRENCY_TYPES, this.clock);
  }

  private ExchangeRateRequestDTO getRequestDTO() {
    Set<CurrencyType> rates = new LinkedHashSet<>(List.of(CurrencyType.PLN));
    return new ExchangeRateRequestDTO(base, rates);
  }
}