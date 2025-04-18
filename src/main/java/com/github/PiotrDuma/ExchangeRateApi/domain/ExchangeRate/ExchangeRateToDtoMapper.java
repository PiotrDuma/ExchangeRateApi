package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import java.util.function.Function;

class ExchangeRateToDtoMapper {

  public static ExchangeRateServiceDto apply(ExchangeRate exchangeRate) {
    return ExchangeRateServiceDto.builder()
        .base(exchangeRate.getBase())
        .exchangeCurrencies(exchangeRate.getExchangeCurrencies())
        .rates(exchangeRate.getExchangeRates())
        .created(exchangeRate.getCreated())
        .updated(exchangeRate.getLastUpdated())
        .convertedSum(exchangeRate.getConvertedSum())
        .build();
  }
}
