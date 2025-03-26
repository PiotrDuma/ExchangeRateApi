package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeRateDTO {

  private CurrencyType base;
  private Map<CurrencyType, Double> rates;
}
