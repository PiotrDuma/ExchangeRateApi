package com.github.PiotrDuma.ExchangeRateApi.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import java.util.Set;
import org.springframework.web.client.RestClientException;

public interface ClientRequestService {
  JsonNode getExchangeRate(CurrencyType base, Set<CurrencyType> target) throws RestClientException;
}
