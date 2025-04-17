package com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface ExchangeRateFacade {
  CurrencyType getBase();
  Set<CurrencyType> getExchangeCurrencies();
  Map<CurrencyType, Double> getExchangeRates();
  Instant getCreated();
  Instant getLastUpdated();
  Double getConvertedSum();
}
