package com.github.PiotrDuma.ExchangeRateApi.api.client;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import org.springframework.web.client.RestClientException;

public interface UpdateExecutorHandler {
  void updateRates(CurrencyType type) throws RestClientException;
}
