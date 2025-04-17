package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("IT")
@Disabled //skip context building and invoking external service
class ClientRequestServiceIT {
  private static final CurrencyType base = CurrencyType.PLN;
  private static final Set<CurrencyType> target =
      new HashSet<>(Arrays.asList(CurrencyType.EUR, CurrencyType.USD));

  @Autowired
  private ClientRequestServiceImpl service;

  @Test
  void getInstanceFromClient(){
    System.out.println(service.getExchangeRate(base, target));
  }
}