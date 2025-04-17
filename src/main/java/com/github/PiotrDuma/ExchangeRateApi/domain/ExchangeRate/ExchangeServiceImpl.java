package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  public ExchangeRateResponseDTO update(CurrencyType baseId,
      List<CurrencyType> exchangeCurrencies) {
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
  public Optional<ExchangeRateResponseDTO> getById(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public void delete(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }
}
