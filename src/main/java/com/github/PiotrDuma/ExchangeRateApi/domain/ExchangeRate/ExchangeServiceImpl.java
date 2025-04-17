package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.exceptions.ResourceNotFoundException;
import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
class ExchangeServiceImpl implements ExchangeService {
  public static final String NOT_FOUND = "Resource with currency base %s not found.";
  private final ExchangeRateRepository repository;
  private final Clock clock;

  @Override
  public ExchangeRateServiceDto create(ExchangeRateCreateDto dto) {
    ExchangeRate entity = new ExchangeRate(dto.getBase(), dto.getExchangeCurrencies(), this.clock);
    ExchangeRate saved = this.repository.save(entity);
    return ExchangeRateToDtoMapper.apply(saved);
  }

  @Override
  public ExchangeRateServiceDto update(CurrencyType baseId,
      List<CurrencyType> exchangeCurrencies) {
    Set<CurrencyType> newTypes = new HashSet<>(exchangeCurrencies);

    ExchangeRate exchangeRate = this.repository.findByBase(baseId)
        .orElseThrow(() -> {
          throw new ResourceNotFoundException(String.format(NOT_FOUND, baseId));
        });

    exchangeRate.updateExchangeCurrencies(newTypes, clock);
    return ExchangeRateToDtoMapper.apply(exchangeRate);
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
