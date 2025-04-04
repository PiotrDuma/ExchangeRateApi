package com.github.PiotrDuma.ExchangeRateApi.domain;

import static com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeController.URI;
import static com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeController.URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.config.FixedClockConfig;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateFacade;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
  private static final CurrencyType BASE = CurrencyType.EUR;
  @MockitoBean
  private ExchangeService service;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Clock clock;
  @Autowired
  private ObjectMapper objectMapper;

  @Captor
  ArgumentCaptor<ExchangeRateRequestDTO> requestDto;
  @Captor
  ArgumentCaptor<List<CurrencyType>> typesCaptor;
  @Captor
  ArgumentCaptor<CurrencyType> baseCaptor;

  @Test
  void postMethodShouldInvokeService() throws Exception {
    when(this.service.create(any())).thenReturn(exchangeRateResponseDTO());

    mockMvc.perform(post(URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(getRequestDTO())))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", containsString(URL + "/" + BASE)));

    verify(this.service, times(1)).create(requestDto.capture());

    assertThat(requestDto.getValue().getBase()).isEqualTo(BASE);
    assertThat(requestDto.getValue().getGetExchangeCurrencies()).isEqualTo(CURRENCY_TYPES);
  }

  @Test
  void getMethodById() throws Exception {
    when(this.service.getById(any())).thenReturn(exchangeRateResponseDTO());

    mockMvc.perform(get(URI, BASE)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.base", is(BASE.toString())))
        .andExpect(jsonPath("$.exchangeCurrencies", hasItem(CurrencyType.USD.toString())));

    verify(this.service, times(1)).getById(baseCaptor.capture());

    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
  }

  @Test
  void getMethod() throws Exception {
    List<ExchangeRateFacade> list = new ArrayList<>(List.of(exchangeRateResponseDTO()));
    list.add(new ExchangeRate(CurrencyType.USD, CURRENCY_TYPES, clock));

    when(this.service.getAll()).thenReturn(list);

    mockMvc.perform(get(URL)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(2)));
  }

  @Test
  void updateMethod() throws Exception{
    mockMvc.perform(put(URI, BASE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new ExchangeController.UpdateDto(CURRENCY_TYPES.stream().toList()))))
        .andExpect(status().isNoContent());

    verify(this.service, times(1)).update(baseCaptor.capture(), typesCaptor.capture());

    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
    assertThat(typesCaptor.getValue()).isEqualTo(CURRENCY_TYPES.stream().toList());
  }

  @Test
  void deleteMethod() throws Exception{
    mockMvc.perform(delete(URI, BASE))
        .andExpect(status().isNoContent());

    verify(this.service, times(1)).delete(baseCaptor.capture());

    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
  }

  private ExchangeRateFacade exchangeRateResponseDTO(){
    return new ExchangeRate(BASE, CURRENCY_TYPES, this.clock);
  }

  private ExchangeRateRequestDTO getRequestDTO() {
    return new ExchangeRateRequestDTO(BASE, CURRENCY_TYPES);
  }
}