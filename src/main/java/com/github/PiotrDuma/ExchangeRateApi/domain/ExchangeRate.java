package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ExchangeRate implements ExchangeRateResponseDTO {
  private CurrencyType base;
  private Set<CurrencyType> exchangeTypes;
  private Map<CurrencyType, Double> rates;
  private Instant created;
  private Instant updated;
  private Double convertedSum;

  ExchangeRate() {
  }

  ExchangeRate(CurrencyType base, Set<CurrencyType> exchangeTypes, Clock clock) {
    this.base = base;
    this.exchangeTypes = exchangeTypes;
    this.rates = new HashMap<>();
    this.created = Instant.now(clock);
    this.updated = created;
    this.convertedSum = 0d;
  }

  public void updateRates(Map<CurrencyType, Double> rates, Clock clock){
    this.rates = rates;
    this.updated = Instant.now(clock);
  }

  public void updateExchangeTypes(Set<CurrencyType> exchangeTypes, Clock clock){
    this.exchangeTypes = exchangeTypes;
    this.updated = Instant.now(clock);
  }

  @Override
  public CurrencyType getBase() {
    return this.base;
  }

  @Override
  public Set<CurrencyType> getExchangeTypes() {
    return this.exchangeTypes;
  }

  @Override
  public Map<CurrencyType, Double> getExchangeRates() {
    return this.rates;
  }

  @Override
  public Instant getCreated() {
    return this.created;
  }

  @Override
  public Instant getLastUpdated() {
    return this.updated;
  }

  @Override
  public Double getConvertedSum() {
    return this.convertedSum;
  }

  protected void setLastUpdated(Instant time){
    this.updated = time;
  }
}
