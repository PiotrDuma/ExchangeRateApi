package com.github.PiotrDuma.ExchangeRateApi.domain;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateFacade;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateRequestDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateResponseDTO;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
class ExchangeController {
  public static final String URL = "/api/currency";
  public static final String PATH_VARIABLE = "baseId";
  public static final String URI = URL + "/{" + PATH_VARIABLE + "}";

  private final ExchangeService exchangeService;

  @PostMapping(URL)
  public ResponseEntity handlePostRequest(@RequestBody ExchangeRateRequestDTO dto){
    ExchangeRateFacade exRate = this.exchangeService.create(dto);

    HttpHeaders headers = new HttpHeaders();
    headers.put("Location", List.of( URL + "/" + exRate.getBase()));
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping(URL)
  public ResponseEntity<List<ExchangeRateResponseDTO>> getAllCurrencies(){
    List<ExchangeRateResponseDTO> response = this.exchangeService.getAll()
        .stream()
        .map(ExchangeRateFacade::toDto)
        .toList();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(URI)
  public ResponseEntity<ExchangeRateResponseDTO> getCurrencyById(@PathVariable(PATH_VARIABLE)
      CurrencyType type){

    ExchangeRateResponseDTO response = this.exchangeService.getById(type).toDto();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping(URI)
  public ResponseEntity updateCurrency(@PathVariable(PATH_VARIABLE) CurrencyType baseId,
      @RequestBody UpdateDto dto){

    this.exchangeService.update(baseId, dto.types());

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(URI)
  public ResponseEntity deleteCurrency(@PathVariable(PATH_VARIABLE) CurrencyType baseId){

    this.exchangeService.delete(baseId);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  public record UpdateDto(List<CurrencyType> types){
    public UpdateDto(List<CurrencyType> types){
      this.types = types;
    }
  }
}
