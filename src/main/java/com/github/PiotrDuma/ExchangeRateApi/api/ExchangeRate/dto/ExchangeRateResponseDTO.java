package com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExchangeRateResponseDTO {
  private CurrencyType base;
  private Set<CurrencyType> exchangeCurrencies;
  private Map<CurrencyType, Double> rates;
  private Instant created;
  private Instant updated;
  private Double convertedSum;
}
