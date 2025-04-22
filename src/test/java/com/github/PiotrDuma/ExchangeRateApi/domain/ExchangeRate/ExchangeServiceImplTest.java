package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.exceptions.ResourceNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class ExchangeServiceImplTest {
  private static final CurrencyType BASE = CurrencyType.USD;
  private static final Set<CurrencyType> TYPES = new HashSet<>(List.of(CurrencyType.EUR));

  @Mock
  private ExchangeRateRepository repo;

  private ExchangeServiceImpl service;

  @Captor
  private ArgumentCaptor<ExchangeRate> exCaptor;
  @Captor
  private ArgumentCaptor<CurrencyType> baseCaptor;
  @Captor
  private ArgumentCaptor<Set<CurrencyType>> typesCaptor;
  @Captor
  private ArgumentCaptor<Map<CurrencyType, Double>> mapCaptor;

  @BeforeEach
  void setUp() {
    this.service = new ExchangeServiceImpl(repo, clock());
  }

  @Test
  void serviceShouldInvokeSaveEntity() {
    ExchangeRateCreateDto dto = new ExchangeRateCreateDto(BASE, TYPES);
    when(this.repo.save(any())).thenReturn(getExchangeRate());

    this.service.create(dto);

    verify(this.repo, times(1)).save(exCaptor.capture());

    ExchangeRate result = exCaptor.getValue();
    assertThat(result.getBase()).isEqualTo(BASE);
    assertThat(result.getExchangeCurrencies()).isEqualTo(TYPES);
    assertThat(result.getExchangeRates()).isNotNull();
    assertThat(result.getExchangeRates().size()).isEqualTo(0);
    assertThat(result.getCreated()).isEqualTo(clock().instant());
    assertThat(result.getLastUpdated()).isEqualTo(clock().instant());
    assertThat(result.getConvertedSum()).isEqualTo(0d);
  }

  @Test
  void createShouldReturnValidDto() {
    ExchangeRateCreateDto dto = new ExchangeRateCreateDto(BASE, TYPES);
    when(this.repo.save(any())).thenReturn(getExchangeRate());

    ExchangeRateServiceDto result = this.service.create(dto);

    assertThat(result.getBase()).isEqualTo(BASE);
    assertThat(result.getExchangeCurrencies()).isEqualTo(TYPES);
    assertThat(result.getRates()).isNotNull();
    assertThat(result.getRates().size()).isEqualTo(0);
    assertThat(result.getCreated()).isEqualTo(clock().instant());
    assertThat(result.getUpdated()).isEqualTo(clock().instant());
    assertThat(result.getConvertedSum()).isEqualTo(0d);
  }

  @Test
  void updateShouldReturnValidDto() {
    List<CurrencyType> updated = List.of(CurrencyType.CHF, CurrencyType.PLN);
    ExchangeRateServiceDto responseDto = getResponseDto();
    responseDto.setExchangeCurrencies(new HashSet<>(updated));

    when(this.repo.findByBase(BASE)).thenReturn(Optional.of(getExchangeRate()));

    ExchangeRateServiceDto result = this.service.update(BASE, updated);

    assertThat(result.getBase()).isEqualTo(BASE);
    assertTrue(result.getExchangeCurrencies().containsAll(updated));
    assertThat(result.getRates()).isNotNull();
    assertThat(result.getRates().size()).isEqualTo(0);
    assertThat(result.getCreated()).isEqualTo(clock().instant());
    assertThat(result.getUpdated()).isEqualTo(clock().instant());
    assertThat(result.getConvertedSum()).isEqualTo(0d);
  }

  @Test
  void updateShouldInvokeValidArguments() {
    List<CurrencyType> updated = List.of(CurrencyType.CHF, CurrencyType.PLN);
    ExchangeRate mock = mock(ExchangeRate.class);

    when(this.repo.findByBase(BASE)).thenReturn(Optional.of(mock));

    ExchangeRateServiceDto result = this.service.update(BASE, updated);

    verify(this.repo, times(1)).findByBase(any());
    verify(mock, times(1)).updateExchangeCurrencies(typesCaptor.capture(), any());

    assertThat(typesCaptor.getValue().containsAll(updated)).isTrue();
  }

  @Test
  void updateShouldThrowWhenRepoNotFoundEntity() {
    List<CurrencyType> updated = List.of(CurrencyType.CHF, CurrencyType.PLN);

    when(this.repo.findByBase(BASE)).thenReturn(Optional.empty());

    ResourceNotFoundException excepton = assertThrows(
        ResourceNotFoundException.class, () -> this.service.update(BASE, updated));

    verify(this.repo, times(1)).findByBase(any());

    assertThat(excepton.getMessage()).isEqualTo(String.format(ExchangeServiceImpl.NOT_FOUND, BASE));
  }

  @Test
  void updateRatesShouldThrowWhenRepoNotFoundEntity() {
    Map<CurrencyType, Double> rates = new HashMap<>();
    rates.put(CurrencyType.AUD, 1234d);

    when(this.repo.findByBase(BASE)).thenReturn(Optional.empty());

    ResourceNotFoundException excepton = assertThrows(
        ResourceNotFoundException.class, () -> this.service.updateRates(BASE, rates));

    verify(this.repo, times(1)).findByBase(any());

    assertThat(excepton.getMessage()).isEqualTo(String.format(ExchangeServiceImpl.NOT_FOUND, BASE));
  }

  @Test
  void updateRatesShouldInvokeValidArguments() {
    Map<CurrencyType, Double> rates = new HashMap<>();
    rates.put(CurrencyType.AUD, 1234d);
    ExchangeRate mock = mock(ExchangeRate.class);

    when(this.repo.findByBase(BASE)).thenReturn(Optional.of(mock));

    ExchangeRateServiceDto result = this.service.updateRates(BASE, rates);

    verify(this.repo, times(1)).findByBase(baseCaptor.capture());
    verify(mock, times(1)).updateRates(mapCaptor.capture(), any());

    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
    assertThat(mapCaptor.getValue()).isEqualTo(rates);
  }

  @Test
  void updateRatesShouldInvokeRepoMethod() {
    Map<CurrencyType, Double> rates = new HashMap<>();
    rates.put(CurrencyType.AUD, 1234d);
    ExchangeRate mock = getExchangeRate();

    when(this.repo.findByBase(BASE)).thenReturn(Optional.of(mock));

    ExchangeRateServiceDto result = this.service.updateRates(BASE, rates);

    assertThat(result.getRates().size()).isEqualTo(1);
    assertThat(result.getRates().containsKey(CurrencyType.AUD)).isTrue();
    assertThat(result.getRates().get(CurrencyType.AUD)).isEqualTo(1234d);
  }

  @Test
  void getAllShouldReturnListOfDtos() {
    ExchangeRate mock1 = getExchangeRate();
    ExchangeRate mock2 = getExchangeRate();
    List<ExchangeRate> getAll = List.of(mock1, mock2);

    when(this.repo.findAll()).thenReturn(getAll);

    List<ExchangeRateServiceDto> result = this.service.getAll();

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0)).isInstanceOf(ExchangeRateServiceDto.class);
  }

  @Test
  void getByIdShouldReturnEmptyOptionalIfRepoFindNoEntity() {
    when(this.repo.findByBase(any())).thenReturn(Optional.empty());

    Optional<ExchangeRateServiceDto> result = this.service.getById(any());

    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void getByIdShouldReturnOptionalOfResponse() {
    Optional<ExchangeRate> entity = Optional.of(getExchangeRate());

    when(this.repo.findByBase(any())).thenReturn(entity);

    Optional<ExchangeRateServiceDto> result = this.service.getById(any());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isInstanceOf(ExchangeRateServiceDto.class);
    assertThat(result.get()).isEqualTo(ExchangeRateToDtoMapper.apply(getExchangeRate()));
  }

  @Test
  void getByIdShouldInvokeGetByIdOnRepository() {
    when(this.repo.findByBase(any())).thenReturn(Optional.empty());
    this.service.getById(BASE);

    verify(this.repo, times(1)).findByBase(baseCaptor.capture());
    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
  }

  @Test
  void deleteShouldThrowResourceNotFoundExceptionIfNotFound() {
    String message = String.format(ExchangeServiceImpl.NOT_FOUND, BASE);

    when(this.repo.findByBase(any())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.service.delete(BASE));

    assertThat(exception.getMessage()).isEqualTo(message);
  }

  @Test
  void deleteShouldInvokeValidMethods() {
    ExchangeRate mock = mock(ExchangeRate.class);
    when(this.repo.findByBase(any())).thenReturn(Optional.of(mock));
    doNothing().when(this.repo).delete(any());

    this.service.delete(BASE);

    verify(this.repo, times(1)).findByBase(baseCaptor.capture());
    verify(this.repo, times(1)).delete(exCaptor.capture());

    assertThat(baseCaptor.getValue()).isEqualTo(BASE);
    assertThat(exCaptor.getValue()).isEqualTo(mock);
  }

  private ExchangeRate getExchangeRate() {
    return new ExchangeRate(BASE, TYPES, clock());
  }

  private ExchangeRateServiceDto getResponseDto() {
    return new ExchangeRateServiceDto(BASE, TYPES, new HashMap<>(), clock().instant(),
        clock().instant(), 0d);
  }

  private Clock clock() {
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"), ZoneId.systemDefault());
  }
}