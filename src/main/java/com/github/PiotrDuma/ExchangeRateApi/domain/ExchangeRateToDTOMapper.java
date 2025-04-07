package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
class ExchangeRateToDTOMapper implements Function<ExchangeRate, ExchangeRateResponseDTO> {

  @Override
  public ExchangeRateResponseDTO apply(ExchangeRate exchangeRate) {
    return ExchangeRateResponseDTO.builder()
        .base(exchangeRate.getBase())
        .exchangeCurrencies(exchangeRate.getExchangeCurrencies())
        .rates(exchangeRate.getExchangeRates())
        .created(exchangeRate.getCreated())
        .updated(exchangeRate.getLastUpdated())
        .convertedSum(exchangeRate.getConvertedSum())
        .build();
  }
}
