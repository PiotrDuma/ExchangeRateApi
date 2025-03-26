package com.github.PiotrDuma.ExchangeRateApi.client;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateDTO;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@AllArgsConstructor
class ClientRequestServiceImpl implements ClientRequestService {
  private final RestTemplateBuilder restTemplateBuilder;
  private static final String v1 = "?currencies=EUR,USD,PLN&base_currency=EUR";

  @Override
  public ExchangeRateDTO getExchangeRate(CurrencyType base, List<CurrencyType> target) {
    log.debug("Send GET request via " + this.getClass().getName());
    RestTemplate restTemplate = restTemplateBuilder.build();

    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(v1);

    ResponseEntity<Map> response =
        restTemplate.getForEntity(uri.toUriString(), Map.class);

    response.getBody().forEach((k, v) -> System.out.println(k + " : " + v));
    System.out.println(response.getBody());
    return null;
  }
}
