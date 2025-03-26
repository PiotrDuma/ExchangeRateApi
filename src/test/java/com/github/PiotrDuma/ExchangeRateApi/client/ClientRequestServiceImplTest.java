package com.github.PiotrDuma.ExchangeRateApi.client;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientRequestServiceImplTest {
  private static final CurrencyType base = CurrencyType.PLN;
  private static final List<CurrencyType> target = Arrays.asList(CurrencyType.EUR, CurrencyType.USD);

  @Autowired
  private ClientRequestService service;

  @Test
  void getInstanceFromClient(){
    System.out.println(service.getExchangeRate(base, target));
  }
}