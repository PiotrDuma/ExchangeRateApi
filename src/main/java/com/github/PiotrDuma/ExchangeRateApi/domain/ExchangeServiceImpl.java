package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ExchangeServiceImpl implements ExchangeService {

  @Override
  public ExchangeRateResponseDTO create(ExchangeRateRequestDTO dto) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public ExchangeRateResponseDTO update(CurrencyType baseId, List<CurrencyType> exchangeTypes) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public ExchangeRateResponseDTO updateRates(Map<CurrencyType, Double> rates) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public List<ExchangeRateResponseDTO> getAll() {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public ExchangeRateResponseDTO getById(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public void delete(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }
}
