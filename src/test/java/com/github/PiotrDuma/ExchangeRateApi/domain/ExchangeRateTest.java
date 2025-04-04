package com.github.PiotrDuma.ExchangeRateApi.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateTest {

  @Mock
  private Clock clock;

  private static final Set<CurrencyType> CURRENCY_TYPES = new HashSet<>(List.of(CurrencyType.USD));

  @BeforeEach
  void setUp() {
    Instant instant = Instant.parse("2025-10-10T10:15:30.00Z");
    when(this.clock.instant()).thenReturn(instant);
  }

  @Test
  void testInitProperties(){
    ExchangeRate obj = getExchangeRate();

    assertThat(obj.getBase()).isEqualTo(CurrencyType.EUR);
    assertThat(obj.getExchangeTypes()).isEqualTo(CURRENCY_TYPES);
    assertThat(obj.getExchangeRates().size()).isEqualTo(0);
    assertThat(obj.getCreated()).isEqualTo(this.clock.instant());
    assertThat(obj.getLastUpdated()).isEqualTo(this.clock.instant());
  }

  @Test
  void updateExcahngeTypesShouldUpdateSetAndLastUpdatedField(){
    ExchangeRate obj = getExchangeRate();
    obj.setLastUpdated(Instant.parse("2000-10-10T11:11:11.00Z"));
    Set<CurrencyType> expected = new HashSet<>(CURRENCY_TYPES);
    expected.add(CurrencyType.JPY);


    assertThat(obj.getExchangeTypes().size()).isEqualTo(1);

    obj.updateExchangeTypes(expected, this.clock);

    assertThat(obj.getExchangeTypes().size()).isEqualTo(2);
    assertThat(obj.getExchangeTypes()).isEqualTo(expected);
    assertThat(obj.getLastUpdated()).isEqualTo(this.clock.instant());
  }

  @Test
  void updateRatesShouldUpdateMapAndLastUpdatedField(){
    ExchangeRate obj = getExchangeRate();
    obj.setLastUpdated(Instant.parse("2000-10-10T11:11:11.00Z"));
    Map<CurrencyType, Double> expected = new HashMap<>();
    expected.put(CurrencyType.JPY, 1234d);


    assertThat(obj.getExchangeRates().size()).isEqualTo(0);

    obj.updateRates(expected, this.clock);

    assertThat(obj.getExchangeRates().size()).isEqualTo(1);
    assertThat(obj.getExchangeRates()).isEqualTo(expected);
    assertThat(obj.getLastUpdated()).isEqualTo(this.clock.instant());
  }

  private ExchangeRate getExchangeRate(){
    return new ExchangeRate(CurrencyType.EUR, CURRENCY_TYPES, this.clock);
  }
}