package com.github.PiotrDuma.ExchangeRateApi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import java.util.Set;
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
  public JsonNode getExchangeRate(CurrencyType base, Set<CurrencyType> target) {
    log.debug("Send GET request via " + this.getClass().getName());
    RestTemplate restTemplate = restTemplateBuilder.build();

    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(v1);

    ResponseEntity<JsonNode> response =
        restTemplate.getForEntity(uri.toUriString(), JsonNode.class);

    return response.getBody();
  }
}
