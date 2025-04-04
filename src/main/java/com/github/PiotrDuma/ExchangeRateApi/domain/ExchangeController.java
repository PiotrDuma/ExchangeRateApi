package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
class ExchangeController {

  private final ExchangeService exchangeService;

  @PostMapping("/api/exchange")
  public ResponseEntity handlePostRequest(@RequestBody ExchangeRateRequestDTO dto){
    ExchangeRateResponseDTO exRate = this.exchangeService.create(dto);

    HttpHeaders headers = new HttpHeaders();
    headers.put("Location", List.of( "/api/exchange/" + exRate.getBase()));
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }
}
