package com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.exceptions.ResourceNotFoundException;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRatePutRequestDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateRequestDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
  public ResponseEntity<ExchangeRateResponseDto> handlePostRequest(
      @Validated @RequestBody ExchangeRateRequestDto dto){
    ExchangeRateCreateDto toServiceDto = ExchangeRateDtoMapper.postRequestDtoToServiceDto(dto);
    ExchangeRateServiceDto exRate = this.exchangeService.create(toServiceDto);

    HttpHeaders headers = new HttpHeaders();
    headers.put("Location", List.of( URL + "/" + exRate.getBase()));

    ExchangeRateResponseDto response = ExchangeRateDtoMapper.serviceDtoToResponseDto(exRate);
    return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
  }

  @GetMapping(URL)
  public ResponseEntity<List<ExchangeRateResponseDto>> getAllCurrencies(){
    List<ExchangeRateServiceDto> list = this.exchangeService.getAll();

    List<ExchangeRateResponseDto> response = list.stream()
        .map(ExchangeRateDtoMapper::serviceDtoToResponseDto)
        .toList();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(URI)
  public ResponseEntity<ExchangeRateResponseDto> getCurrencyById(
      @PathVariable(PATH_VARIABLE) CurrencyType type){

    ExchangeRateServiceDto serviceDto = this.exchangeService.getById(type)
        .orElseThrow(ResourceNotFoundException::new);

    ExchangeRateResponseDto response = ExchangeRateDtoMapper.serviceDtoToResponseDto(serviceDto);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping(URI)
  public ResponseEntity<ExchangeRateResponseDto> updateCurrency(
      @PathVariable(PATH_VARIABLE) CurrencyType baseId,
      @Validated @RequestBody ExchangeRatePutRequestDto dto){

    ExchangeRateServiceDto updated = this.exchangeService.update(baseId, dto.types());
    ExchangeRateResponseDto response = ExchangeRateDtoMapper.serviceDtoToResponseDto(updated);

    return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(URI)
  public ResponseEntity<?> deleteCurrency(@PathVariable(PATH_VARIABLE) CurrencyType baseId){

    this.exchangeService.delete(baseId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
