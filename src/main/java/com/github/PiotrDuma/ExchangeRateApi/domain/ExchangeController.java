package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateFacade;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    ExchangeRateFacade exRate = this.exchangeService.create(dto);

    HttpHeaders headers = new HttpHeaders();
    headers.put("Location", List.of( "/api/exchange/" + exRate.getBase()));
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping("/api/exchange")
  public ResponseEntity<List<ExchangeRateResponseDTO>> getAllCurrencies(){
    List<ExchangeRateResponseDTO> response = this.exchangeService.getAll()
        .stream()
        .map(ExchangeRateFacade::toDto)
        .toList();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/api/exchange/{baseId}")
  public ResponseEntity<ExchangeRateResponseDTO> getCurrencyById(@PathParam("baseId")
      CurrencyType type){

    ExchangeRateResponseDTO response = this.exchangeService.getById(type).toDto();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
