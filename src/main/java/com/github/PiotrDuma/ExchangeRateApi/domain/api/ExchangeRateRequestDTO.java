package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeRateRequestDTO {

  private CurrencyType base;
  private Set<CurrencyType> getExchangeCurrencies;

  public ExchangeRateRequestDTO(CurrencyType base, Set<CurrencyType> getExchangeCurrencies) {
    this.base = base;
    this.getExchangeCurrencies = getExchangeCurrencies;
  }
}
