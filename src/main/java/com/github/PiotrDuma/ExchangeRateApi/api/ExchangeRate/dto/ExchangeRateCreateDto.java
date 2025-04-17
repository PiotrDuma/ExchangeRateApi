package com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExchangeRateCreateDto {
  private CurrencyType base;
  private Set<CurrencyType> exchangeCurrencies;
}
