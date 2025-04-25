package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.config.FixedClockConfig;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.exceptions.ResourceNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(FixedClockConfig.class)
@Tag("IT")
@ActiveProfiles(profiles = "test")
class ExchangeServiceIT {

  private static final CurrencyType BASE = CurrencyType.USD;
  private static final Set<CurrencyType> TYPES =
      new HashSet<>(List.of(CurrencyType.EUR, CurrencyType.KRW));

  @Autowired
  private ExchangeRateRepository repo;
  @Autowired
  private ExchangeServiceImpl service;

  @Test
  @Transactional
  @Rollback
  void createShouldAddEntityToDatabase() {
    CurrencyType newBase = CurrencyType.AUD;
    ExchangeRateCreateDto dto = new ExchangeRateCreateDto(newBase, TYPES);

    assertThat(this.repo.findAll().size()).isEqualTo(2);
    ExchangeRateServiceDto output = this.service.create(dto);
    repo.flush();

    Optional<ExchangeRate> result = this.repo.findByBase(output.getBase());

    assertThat(output.getBase()).isEqualTo(newBase);
    assertThat(output.getExchangeCurrencies()).isEqualTo(TYPES);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getBase()).isEqualTo(newBase);
    assertThat(result.get().getExchangeCurrencies()).isEqualTo(TYPES);

    assertThat(this.repo.findAll().size()).isEqualTo(3);
  }

  @Test
  @Transactional
  void getAllShouldReturnValidDatabaseCollection() {
    List<ExchangeRateServiceDto> output = this.service.getAll();

    assertThat(output.size()).isEqualTo(2);
  }

  @Test
  @Transactional
  void getByIdShouldReturnValidEntity() {
    Optional<ExchangeRateServiceDto> output = this.service.getById(BASE);

    assertThat(output.isPresent()).isTrue();
    assertThat(output.get().getBase()).isEqualTo(BASE);
    assertThat(output.get().getRates().size()).isEqualTo(2);
    assertThat(output.get().getExchangeCurrencies().size()).isEqualTo(2);
  }

  @Test
  @Transactional
  void getByIdShouldReturnEmptyOptionalIfNotFound() {
    Optional<ExchangeRateServiceDto> output = this.service.getById(CurrencyType.KRW);

    assertThat(output.isEmpty()).isTrue();
  }

  @Test
  @Transactional
  @Rollback
  void updateShouldAffectEntityInRepo() {
    ExchangeRateServiceDto update = this.service.update(BASE, TYPES.stream().toList());
    this.repo.flush();
    Optional<ExchangeRate> result = this.repo.findByBase(BASE);

    assertThat(update.getExchangeCurrencies()).isEqualTo(TYPES);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getExchangeCurrencies()).isEqualTo(TYPES);
  }

  @Test
  @Transactional
  void updateShouldThrowWhenEntityNotFound() {
    String expected = String.format(ExchangeServiceImpl.NOT_FOUND, CurrencyType.KRW);
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> this.service.update(CurrencyType.KRW, TYPES.stream().toList()));

    assertThat(exception.getMessage()).isEqualTo(expected);
  }

  @Test
  @Transactional
  void updateRatesShouldAffectEntityInRepository() {
    CurrencyType key = CurrencyType.KRW;
    Double value = 2222d;
    Map<CurrencyType, Double> rates = new HashMap<>();
    rates.put(key, value);

    ExchangeRateServiceDto update = this.service.updateRates(BASE, rates);
    this.repo.flush();
    Optional<ExchangeRate> result = this.repo.findByBase(BASE);

    assertThat(update.getRates().size()).isEqualTo(1);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getExchangeRates().size()).isEqualTo(1);
    assertThat(result.get().getExchangeRates().containsKey(key)).isTrue();
    assertThat(result.get().getExchangeRates().get(key)).isEqualTo(value);
  }

  @Test
  @Transactional
  void updateRatesShouldThrowWhenEntityNotFound() {
    Map<CurrencyType, Double> rates = new HashMap<>();
    String expected = String.format(ExchangeServiceImpl.NOT_FOUND, CurrencyType.KRW);
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> this.service.updateRates(CurrencyType.KRW, rates));

    assertThat(exception.getMessage()).isEqualTo(expected);
  }

  @Test
  @Transactional
  @Rollback
  void deleteShouldRemoveEntity() {
    UUID id = UUID.fromString("9ce00e0f-0454-45df-9786-3cf24c28ea24");
    assertThat(this.repo.findAll().size()).isEqualTo(2);
    this.service.delete(BASE);
    this.repo.flush();

    assertThat(this.repo.existsByBase(BASE)).isFalse();
    assertThat(this.repo.existsById(id)).isFalse();
    assertThat(this.repo.findAll().size()).isEqualTo(1);
  }

  @Test
  @Transactional
  void deleteShouldThrowIfEntityNotFound() {
    String expected = String.format(ExchangeServiceImpl.NOT_FOUND, CurrencyType.KRW);
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> this.service.delete(CurrencyType.KRW));

    assertThat(exception.getMessage()).isEqualTo(expected);
  }
}