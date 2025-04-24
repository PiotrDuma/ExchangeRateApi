package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestService;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
class UpdateExecutorHandlerImplTest {
  private static final CurrencyType BASE = CurrencyType.USD;
  @Mock
  private ExchangeService exchangeService;

  @Mock
  private ClientRequestService clientRequestService;
  private ObjectMapper objectMapper;
  private ExchangeRateServiceDto exRateDto;

  @InjectMocks
  private UpdateExecutorHandlerImpl service;

  @Captor
  private ArgumentCaptor<Map<CurrencyType, Double>> mapCaptor;

  @BeforeEach
  void setUp(){
    this.objectMapper = new ObjectMapper();
    this.exRateDto = Mockito.mock(ExchangeRateServiceDto.class);
  }

  @Test
  void shouldConsumeRestClientExceptionWhenCurrencyIsNotFound(){
    when(this.exchangeService.getById(any())).thenReturn(Optional.empty());

    this.service.updateRates(BASE);

    verify(this.clientRequestService, times(0)).getExchangeRate(any(), any());
    verify(this.exchangeService, times(0)).updateRates(any(), any());
  }

  @Test
  void invokeUpdateExchangeServiceWhenClientReturnsValidObject() throws Exception{
    ArgumentCaptor<CurrencyType> typeCaptor = ArgumentCaptor.forClass(CurrencyType.class);

    when(this.exchangeService.getById(any())).thenReturn(Optional.of(exRateDto));
    when(exRateDto.getExchangeCurrencies()).thenReturn(Set.of(CurrencyType.KRW));
    when(exRateDto.getBase()).thenReturn(BASE);
    when(this.clientRequestService.getExchangeRate(any(), any())).thenReturn(getJsonNode());

    this.service.updateRates(BASE);

    verify(this.exchangeService, times(1))
        .updateRates(typeCaptor.capture(), mapCaptor.capture());
    Map<CurrencyType, Double> resultMap = mapCaptor.getValue();

    assertThat(typeCaptor.getValue()).isEqualTo(BASE);
    assertThat(resultMap.size()).isEqualTo(1);
    assertThat(resultMap.containsKey(CurrencyType.KRW)).isTrue();
    assertThat(resultMap.get(CurrencyType.KRW)).isEqualTo(123d);
  }

  @Test
  void serviceShouldCatchExceptionAndStopTransactionWhenClientThrowsException(){
    when(this.exchangeService.getById(any())).thenReturn(Optional.of(exRateDto));
    doThrow(RestClientException.class).when(this.clientRequestService).getExchangeRate(any(), any());

    this.service.updateRates(BASE);

    verify(this.exchangeService, times(0)).updateRates(any(), any());
  }

  @Test
  void serviceShouldCatchExceptionAndStopTransactionWhenJsonParsingThrowsException(){
    JsonNode node = mock(JsonNode.class);

    when(this.exchangeService.getById(any())).thenReturn(Optional.of(exRateDto));
    when(this.clientRequestService.getExchangeRate(any(), any())).thenReturn(node);

    this.service.updateRates(BASE);

    verify(this.exchangeService, times(0)).updateRates(any(), any());
  }

  @Test
  void shouldUpdateRatesWithReceivedJsonWithoutBaseType() throws Exception{
    CurrencyType newBase = CurrencyType.PLN;

    when(this.exchangeService.getById(any())).thenReturn(Optional.of(exRateDto));
    when(exRateDto.getExchangeCurrencies()).thenReturn(Set.of());
    when(exRateDto.getBase()).thenReturn(mock(CurrencyType.class));
    when(this.clientRequestService.getExchangeRate(any(), any())).thenReturn(getJsonNodeWithBaseExchange());

    this.service.updateRates(newBase);

    verify(this.exchangeService, times(1))
        .updateRates(any(), mapCaptor.capture());
    Map<CurrencyType, Double> resultMap = mapCaptor.getValue();

    assertThat(resultMap.size()).isEqualTo(3);
    assertThat(resultMap.containsKey(newBase)).isFalse();
  }

  private JsonNode getJsonNode() throws Exception{
    String json = """
      {
          "data": {
              "KRW": 123
          }
      }
      """;
    return objectMapper.readTree(json);
  }

  private JsonNode getJsonNodeWithBaseExchange() throws Exception{
    String json = """
    {
        "data": {
            "CAD": 0.3678174872,
            "EUR": 0.2340384378,
            "PLN": 1,
            "USD": 0.2652083357
        }
    }
      """;
    return objectMapper.readTree(json);
  }
}