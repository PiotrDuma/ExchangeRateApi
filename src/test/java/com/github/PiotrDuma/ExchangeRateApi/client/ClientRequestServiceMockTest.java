package com.github.PiotrDuma.ExchangeRateApi.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

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
import org.springframework.beans.factory.annotation.Value;
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
  @Value("${rest.client.headerTokenKey}")
  private String headerTokenKey;
  @Value("${rest.client.token}")
  private String token;
  private String serverResponse;
  private String URI;

  private MockRestServiceServer server;
  private ClientRequestServiceImpl service;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private RestTemplateBuilder mockRestTemplateBuilder =
      new RestTemplateBuilder(new MockServerRestTemplateCustomizer());


  @BeforeEach
  void setUp() throws JsonProcessingException{
    RestTemplate restTemplate = this.restTemplateBuilder.build();
    server = MockRestServiceServer.bindTo(restTemplate).build();
    when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
    this.service = new ClientRequestServiceImpl(mockRestTemplateBuilder);
    URI = restTemplate.getUriTemplateHandler()
        .expand("?base_currency=EUR&currencies=EUR,USD,PLN").toString();
    this.serverResponse = objectMapper.writeValueAsString(mockClientResponse());
  }

  @Test
  void assertResponseRequestFromClient() {
    server.expect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess(serverResponse, MediaType.APPLICATION_JSON));

    JsonNode dto = this.service.getExchangeRate(CurrencyType.EUR, getTarget());
    //provided hashset may fail test URI comparison due to random iteration
    assertEquals(serverResponse, dto.toString());
  }

  @Test
  void testServiceURIBuilder(){
    server.expect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andExpect(MockRestRequestMatchers.requestTo(URI))
        .andRespond(MockRestResponseCreators.withSuccess(serverResponse, MediaType.APPLICATION_JSON));

    JsonNode dto = this.service.getExchangeRate(CurrencyType.EUR, getTarget());

    server.verify();
  }

  @Test
  void testRequestHeaderFromClient() {
    server.expect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andExpect(header(headerTokenKey, token))
        .andRespond(MockRestResponseCreators.withSuccess(serverResponse, MediaType.APPLICATION_JSON));;

    JsonNode dto = this.service.getExchangeRate(CurrencyType.EUR, getTarget());

    server.verify();
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