package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestService;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@AllArgsConstructor
class ClientRequestServiceImpl implements ClientRequestService {
  private final RestTemplateBuilder restTemplateBuilder;

  @Override
  public JsonNode getExchangeRate(CurrencyType base, Set<CurrencyType> target) throws RestClientException {
    log.debug("Send GET request via " + this.getClass().getName());
    RestTemplate restTemplate = restTemplateBuilder.build();

    UriComponentsBuilder uri = UriComponentsBuilder.newInstance()
        .queryParam("base_currency", base)
        .queryParam("currencies", extractSetToArgument(base, target));

    ResponseEntity<JsonNode> response =
        restTemplate.getForEntity(uri.toUriString(), JsonNode.class);

    return response.getBody();
  }

  private String extractSetToArgument(CurrencyType base, Set<CurrencyType> set){
    StringBuilder str = new StringBuilder().append(base);
    set.remove(base);
    for(CurrencyType type : set){
       str.append(",").append(type);
    }
    return str.toString();
  }
}
