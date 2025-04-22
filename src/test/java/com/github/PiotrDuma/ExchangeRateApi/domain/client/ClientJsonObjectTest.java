package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.client.ClientJsonObject.ExchangeRateData;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("UnitTest")
class ClientJsonObjectTest {
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp(){
    this.objectMapper = new ObjectMapper();
  }

  @Test
  void shouldProperlyReadJson() throws Exception{
    String json = """
      {
          "data": {
              "CAD": 0.3730393703,
              "EUR": 0.2341132379,
              "PLN": 1,
              "USD": 0.269644228
          }
      }
      """;
    ExchangeRateData result = objectMapper.readValue(json, ExchangeRateData.class);

    assertThat(result).isNotNull();
    assertThat(result.data()).isNotNull();

    Map<CurrencyType, Double> map = result.data().rates();
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(CurrencyType.CAD)).isEqualTo(0.3730393703d);
    assertThat(map.get(CurrencyType.EUR)).isEqualTo(0.2341132379d);
    assertThat(map.get(CurrencyType.PLN)).isEqualTo(1d);
    assertThat(map.get(CurrencyType.USD)).isEqualTo(0.269644228d);
  }

  @Test
  void shouldIgnoreInvalidTypes() throws Exception{
    String json = """
      {
          "data": {
              "USD": 0.269644228,
              "INVALID": 2222
          }
      }
      """;
    ExchangeRateData result = objectMapper.readValue(json, ExchangeRateData.class);

    assertThat(result).isNotNull();
    assertThat(result.data()).isNotNull();

    Map<CurrencyType, Double> map = result.data().rates();
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.containsKey(CurrencyType.USD)).isTrue();
  }

  @Test
  void shouldReturnEmptyMap() throws Exception {
    String json = """
      {
          "data": {}
      }
      """;
    ExchangeRateData result = objectMapper.readValue(json, ExchangeRateData.class);

    assertThat(result).isNotNull();
    assertThat(result.data()).isNotNull();

    Map<CurrencyType, Double> map = result.data().rates();
    assertThat(map.size()).isEqualTo(0);
  }
}