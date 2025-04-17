package com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateCreateDto;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateRequestDto;
import com.github.PiotrDuma.ExchangeRateApi.infrastructure.web.ExchangeRate.dto.ExchangeRateResponseDto;

class ExchangeRateDtoMapper {

  public static ExchangeRateResponseDto serviceDtoToResponseDto(final ExchangeRateServiceDto dto){
    return ExchangeRateResponseDto.builder()
        .base(dto.getBase())
        .exchangeCurrencies(dto.getExchangeCurrencies())
        .rates(dto.getRates())
        .created(dto.getCreated())
        .updated(dto.getUpdated())
        .build();
  }

  public static ExchangeRateCreateDto postRequestDtoToServiceDto(final ExchangeRateRequestDto dto){
    return ExchangeRateCreateDto.builder()
        .base(dto.getBase())
        .exchangeCurrencies(dto.getExchangeCurrencies())
        .build();
  }
}
