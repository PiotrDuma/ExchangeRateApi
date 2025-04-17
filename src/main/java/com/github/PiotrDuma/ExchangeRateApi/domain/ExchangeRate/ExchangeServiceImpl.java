package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ExchangeServiceImpl implements ExchangeService {

  @Override
  public ExchangeRateServiceDto create(ExchangeRateCreateDto dto) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public ExchangeRateServiceDto update(CurrencyType baseId,
      List<CurrencyType> exchangeCurrencies) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public ExchangeRateServiceDto updateRates(Map<CurrencyType, Double> rates) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public List<ExchangeRateServiceDto> getAll() {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public Optional<ExchangeRateServiceDto> getById(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }

  @Override
  public void delete(CurrencyType baseId) {
    throw new RuntimeException("Missing implementation");
  }
}
