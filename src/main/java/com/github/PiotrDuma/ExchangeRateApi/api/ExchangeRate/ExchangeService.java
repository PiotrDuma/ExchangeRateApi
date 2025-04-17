package com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExchangeService {
  ExchangeRateServiceDto create(ExchangeRateCreateDto dto);
  ExchangeRateServiceDto update(CurrencyType baseId, List<CurrencyType> exchangeCurrencies);
  ExchangeRateServiceDto updateRates(CurrencyType baseId, Map<CurrencyType, Double> rates);
  List<ExchangeRateServiceDto> getAll();
  Optional<ExchangeRateServiceDto> getById(CurrencyType baseId);
  void delete(CurrencyType baseId);
}
