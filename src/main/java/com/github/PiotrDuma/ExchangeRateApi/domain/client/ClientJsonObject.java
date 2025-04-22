package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ClientJsonObject {
  private static final String EXCEPTION = "ClientJsonObject: Json mapping failure. ";
  private static final String JSON_FIELD = "Missing 'data' field in JSON";

  public record ExchangeRateData(ExchangeRateRates data){
    @JsonCreator
    public static ExchangeRateData parse(JsonNode jsonNode) throws IllegalArgumentException{
      JsonNode node = jsonNode.get("data");
      if (node == null){
        log.error(EXCEPTION + JSON_FIELD);
        throw new IllegalArgumentException(EXCEPTION + JSON_FIELD);
      }
      ExchangeRateRates data = ExchangeRateRates.parse(node);
      return new ExchangeRateData(data);
    }
  }

  public record ExchangeRateRates(Map<CurrencyType, Double> rates){
    @JsonCreator
    public static ExchangeRateRates parse(JsonNode jsonNode) {
      Map<CurrencyType, Double> map = new HashMap<>();

      Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();

      while(iterator.hasNext()){
        Map.Entry<String, JsonNode> entry = iterator.next();
        try {
          map.put(CurrencyType.valueOf(entry.getKey()), entry.getValue().asDouble());
        } catch (IllegalArgumentException e) {
          log.error(EXCEPTION + e.getMessage());
        }
      }
      return new ExchangeRateRates(map);
    }
  }
}
