package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.time.Instant;
import java.util.Set;

public interface ExchangeRateResponseDTO {
  CurrencyType getBase();
  Set<CurrencyType> getRates();
  Instant getCreated();
  Instant getLastUpdated();
  Double getConvertedSum();
}
