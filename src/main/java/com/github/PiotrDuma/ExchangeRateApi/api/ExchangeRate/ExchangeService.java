package com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateRequestDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExchangeService {
  ExchangeRateResponseDTO create(ExchangeRateRequestDTO dto);
  ExchangeRateResponseDTO update(CurrencyType baseId, List<CurrencyType> exchangeCurrencies);
  ExchangeRateResponseDTO updateRates(Map<CurrencyType, Double> rates);
  List<ExchangeRateResponseDTO> getAll();
  Optional<ExchangeRateResponseDTO> getById(CurrencyType baseId);
  void delete(CurrencyType baseId);
}
