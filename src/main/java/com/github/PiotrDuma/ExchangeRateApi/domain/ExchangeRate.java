package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import java.time.Instant;
import java.util.Set;


class ExchangeRate implements ExchangeRateResponseDTO {

  @Override
  public CurrencyType getBase() {
    return null;
  }

  @Override
  public Set<CurrencyType> getRates() {
    return null;
  }

  @Override
  public Instant getCreated() {
    return null;
  }

  @Override
  public Instant getLastUpdated() {
    return null;
  }

  @Override
  public Double getConvertedSum() {
    return null;
  }
}
