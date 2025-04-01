package com.github.PiotrDuma.ExchangeRateApi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import java.util.Set;

public interface ClientRequestService {
  JsonNode getExchangeRate(CurrencyType base, Set<CurrencyType> target);
}
