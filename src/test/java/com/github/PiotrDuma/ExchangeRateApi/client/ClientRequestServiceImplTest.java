package com.github.PiotrDuma.ExchangeRateApi.client;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
    service.getExchangeRate(base, target);
  }
}