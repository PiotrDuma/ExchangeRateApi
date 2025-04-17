package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate.ExchangeRate;
import com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate.ExchangeRateToDTOMapper;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateResponseDTO;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class ExchangeRateToDTOMapperTest {

  private ExchangeRateToDTOMapper mapper = new ExchangeRateToDTOMapper();

  @Test
  void testToDtoMethod(){
    ExchangeRate exRate = getExchangeRate();
    ExchangeRateResponseDTO result = this.mapper.apply(exRate);

    assertThat(result.getBase()).isEqualTo(exRate.getBase());
    assertThat(result.getExchangeCurrencies()).isEqualTo(exRate.getExchangeCurrencies());
    assertThat(result.getRates()).isEqualTo(exRate.getExchangeRates());
    assertThat(result.getCreated()).isEqualTo(exRate.getCreated());
    assertThat(result.getUpdated()).isEqualTo(exRate.getLastUpdated());
    assertThat(result.getConvertedSum()).isEqualTo(exRate.getConvertedSum());
  }

  private ExchangeRate getExchangeRate(){

    return new ExchangeRate(CurrencyType.EUR, new HashSet<>(List.of(CurrencyType.USD)), getClock());
  }

  private Clock getClock(){
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"), ZoneId.systemDefault());
  }
}