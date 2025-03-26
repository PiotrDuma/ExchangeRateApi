package com.github.PiotrDuma.ExchangeRateApi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
class ClientRestTemplateBuilderConfig {
  @Value("${rest.client.url}")
  private String url;
  @Value("${rest.client.headerTokenKey}")
  private String headerTokenKey;
  @Value("${rest.client.token}")
  private String token;

  @Bean
  public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){
    assert url != null;
    assert headerTokenKey != null;
    assert token != null;

    RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
    DefaultUriBuilderFactory uriBuilderFactory = new
        DefaultUriBuilderFactory(url);

    return builder.uriTemplateHandler(uriBuilderFactory).defaultHeader(headerTokenKey, token);
  }
}
