package com.github.PiotrDuma.ExchangeRateApi.domain.api;

import java.util.List;
import java.util.Map;

public interface ExchangeService {
  ExchangeRateResponseDTO create(ExchangeRateRequestDTO dto);
  ExchangeRateResponseDTO update(CurrencyType baseId, List<CurrencyType> exchangeTypes);
  ExchangeRateResponseDTO updateRates(Map<CurrencyType, Double> rates);
  List<ExchangeRateResponseDTO> getAll();
  ExchangeRateResponseDTO getById(CurrencyType baseId);
  void delete(CurrencyType baseId);
}
