package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {
  private static final CurrencyType BASE = CurrencyType.USD;
  private static final Set<CurrencyType> TYPES = new HashSet<>(List.of(CurrencyType.EUR));

  @Mock
  private ExchangeRateRepository repo;

  private ExchangeServiceImpl service;

  @Captor
  ArgumentCaptor<ExchangeRate> exCaptor;

  @BeforeEach
  void setUp(){
    this.service = new ExchangeServiceImpl(repo, clock());
  }

  @Test
  void serviceShouldInvokeSaveEntity(){
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
  void createShouldReturnValidDto(){
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
  void updateShouldReturnValidDto(){
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

  private ExchangeRate getExchangeRate(){
    return new ExchangeRate(BASE, TYPES, clock());
  }

  private ExchangeRateServiceDto getResponseDto(){
    return new ExchangeRateServiceDto(BASE, TYPES, new HashMap<>(), clock().instant(),
        clock().instant(), 0d);
  }

  private Clock clock(){
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"), ZoneId.systemDefault());
  }
}