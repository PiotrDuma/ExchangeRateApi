package com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateRequestDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateResponseDto;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExchangeRateDtoMapperTest {
  private static final CurrencyType BASE = CurrencyType.USD;
  private static final Set<CurrencyType> TYPES = new HashSet<>(List.of(CurrencyType.JPY));

  @Test
  void validateResponseDtoMapping(){
    Map<CurrencyType, Double> map = new HashMap<>();
    map.put(CurrencyType.JPY, 1234d);

    ExchangeRateServiceDto expected = new ExchangeRateServiceDto(BASE, TYPES, map,
        clock().instant(), clock().instant(), 1000d);

    ExchangeRateResponseDto result = ExchangeRateDtoMapper.serviceDtoToResponseDto(expected);

    assertThat(result.getBase()).isEqualTo(expected.getBase());
    assertThat(result.getExchangeCurrencies()).isEqualTo(expected.getExchangeCurrencies());
    assertThat(result.getRates()).isEqualTo(expected.getRates());
    assertThat(result.getCreated()).isEqualTo(expected.getCreated());
    assertThat(result.getUpdated()).isEqualTo(expected.getUpdated());
  }

  @Test
  void validateRequestDtoMapping(){
    ExchangeRateRequestDto expected = new ExchangeRateRequestDto(BASE, TYPES);

    ExchangeRateCreateDto result = ExchangeRateDtoMapper.postRequestDtoToServiceDto(expected);

    assertThat(result.getBase()).isEqualTo(expected.getBase());
    assertThat(result.getExchangeCurrencies()).isEqualTo(expected.getExchangeCurrencies());
  }

  private Clock clock(){
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"),ZoneId.systemDefault());
  }
}