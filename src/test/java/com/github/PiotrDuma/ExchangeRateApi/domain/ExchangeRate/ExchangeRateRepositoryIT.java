package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.config.FixedClockConfig;
import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Tag("IT")
@ActiveProfiles(profiles = "test")
@Import(FixedClockConfig.class)
class ExchangeRateRepositoryIT {
  private static final UUID USD_ID = UUID.fromString("9ce00e0f-0454-45df-9786-3cf24c28ea24");
  @Autowired
  private Clock clock;

  @Autowired
  private ExchangeRateRepository repo;

  @Test
  void testDatabaseDataSize(){
    assertThat(this.repo.findAll().size()).isEqualTo(2);
  }

  @Test
  @Transactional
  void testGetDatabaseDataElement(){
    Optional<ExchangeRate> result = this.repo.findById(USD_ID);

    assertTrue(result.isPresent());
    ExchangeRate obj = result.get();

    assertThat(obj.getBase()).isEqualTo(CurrencyType.USD);
    assertThat(obj.getExchangeCurrencies().size()).isEqualTo(2);
    assertThat(obj.getExchangeRates().size()).isEqualTo(2);
    assertTrue(obj.getExchangeCurrencies().containsAll(List.of(CurrencyType.JPY, CurrencyType.EUR)));
    assertThat(obj.getExchangeRates().get(CurrencyType.EUR)).isEqualTo(1.1d);
    assertThat(obj.getExchangeRates().get(CurrencyType.JPY)).isEqualTo(123d);
  }

  @Transactional
  @Rollback
  @Test
  void testSaveDatabaseElement(){
    Set<CurrencyType> types = new HashSet<>(List.of(CurrencyType.CHF));
    ExchangeRate expected = new ExchangeRate(CurrencyType.AUD, types, clock);

    ExchangeRate result = this.repo.save(expected);
    this.repo.flush();

    assertThat(this.repo.findAll().size()).isEqualTo(3);
    assertTrue(this.repo.findAll().contains(result));
    assertTrue(this.repo.findById(result.getId()).isPresent());
    assertThat(this.repo.findById(result.getId()).get()).isEqualTo(expected);
  }
}