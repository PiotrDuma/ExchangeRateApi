package com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ExchangeRatePutRequestDto(@NotNull(message = NULL_VALUE)
                                        @NotEmpty(message = EMPTY_VALUE) List<CurrencyType> types) {

  public static final String NULL_VALUE = "Field cannot be null.";
  public static final String EMPTY_VALUE = "Field cannot be empty.";
}
