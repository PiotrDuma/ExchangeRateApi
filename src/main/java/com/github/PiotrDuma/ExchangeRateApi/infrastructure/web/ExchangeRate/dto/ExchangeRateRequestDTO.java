package com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeRateRequestDTO {
  public static final String NULL_VALUE = "Field cannot be null.";
  public static final String EMPTY_VALUE = "Field cannot be empty.";

  @NotNull(message = NULL_VALUE)
  private CurrencyType base;
  @NotNull(message = NULL_VALUE)
  @NotEmpty(message = EMPTY_VALUE)
  private Set<CurrencyType> exchangeCurrencies;

  public ExchangeRateRequestDTO(CurrencyType base, Set<CurrencyType> exchangeCurrencies) {
    this.base = base;
    this.exchangeCurrencies = exchangeCurrencies;
  }
}
