package com.github.PiotrDuma.ExchangeRateApi.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FixedClockConfig {
  private static final Instant NOW = Instant.parse("2025-10-10T10:15:30.00Z");
  private static final ZoneId ZONE_ID = ZoneId.systemDefault();

  @Bean
  public Clock clock(){
    return Clock.fixed(NOW, ZONE_ID);
  }
}
