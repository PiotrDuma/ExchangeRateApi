package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.util.List;
import java.util.Map;

public interface ExchangeService {
  ExchangeRateFacade create(ExchangeRateRequestDTO dto);
  ExchangeRateFacade update(CurrencyType baseId, List<CurrencyType> exchangeCurrencies);
  ExchangeRateFacade updateRates(Map<CurrencyType, Double> rates);
  List<ExchangeRateFacade> getAll();
  ExchangeRateFacade getById(CurrencyType baseId);
  void delete(CurrencyType baseId);
}
