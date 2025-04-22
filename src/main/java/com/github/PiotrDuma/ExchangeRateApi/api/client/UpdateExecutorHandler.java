package com.github.PiotrDuma.ExchangeRateApi.api.client;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;

public interface UpdateExecutorHandler {
  void updateRates(CurrencyType type);
}
