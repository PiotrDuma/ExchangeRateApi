package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface ExchangeRateResponseDTO {
  CurrencyType getBase();
  Set<CurrencyType> getExchangeCurrencies();
  Map<CurrencyType, Double> getExchangeRates();
  Instant getCreated();
  Instant getLastUpdated();
  Double getConvertedSum();
}
