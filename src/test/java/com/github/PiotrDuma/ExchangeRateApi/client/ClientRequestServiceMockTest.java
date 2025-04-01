package com.github.PiotrDuma.ExchangeRateApi.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RestClientTest
@Import(ClientRestTemplateBuilderConfig.class)
class ClientRequestServiceMockTest {
  private ClientRequestService service;
  private MockRestServiceServer server;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private RestTemplateBuilder mockRestTemplateBuilder =
      new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

  private String URI;

  @BeforeEach
  void setUp() {
    RestTemplate restTemplate = this.restTemplateBuilder.build();
    server = MockRestServiceServer.bindTo(restTemplate).build();
    when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
    this.service = new ClientRequestServiceImpl(mockRestTemplateBuilder);
    URI = restTemplate.getUriTemplateHandler()
        .expand("?base_currency=EUR&currencies=EUR,USD,PLN").toString();
  }

  @Test
  void getRequestFromClient() throws JsonProcessingException {
    String response = objectMapper.writeValueAsString(mockClientResponse());
    server.expect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andExpect(MockRestRequestMatchers.requestTo(URI))
        .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_JSON));

    JsonNode dto = this.service.getExchangeRate(CurrencyType.EUR, getTarget());
    //provided hashset may fail test URI comparison due to random iteration
    assertEquals(response, dto.toString());
  }

  private Set<CurrencyType> getTarget() {
    return new TreeSet<>(Arrays.asList(CurrencyType.USD, CurrencyType.PLN, CurrencyType.EUR));
  }

  private Map<CurrencyType, Double> getResponseMap(){
    Map<CurrencyType, Double> data = new HashMap<>();
      data.put(CurrencyType.USD, 0.91);
      data.put(CurrencyType.PLN, 4.20);
      data.put(CurrencyType.EUR, 1.0);
    return data;
  }

  private Map<String, Map<CurrencyType, Double>> mockClientResponse(){
    HashMap<String, Map<CurrencyType, Double>> response = new HashMap<>();
    response.put("data", getResponseMap());
    return response;
  }
}