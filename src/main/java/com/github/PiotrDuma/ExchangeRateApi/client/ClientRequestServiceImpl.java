package com.github.PiotrDuma.ExchangeRateApi.client;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
class ClientRequestServiceImpl implements ClientRequestService {
  private final RestTemplateBuilder restTemplateBuilder;

  @Override
  public ExchangeRateDTO getExchangeRate(CurrencyType base, List<CurrencyType> target) {
    return null;
  }
}
