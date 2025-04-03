package com.github.PiotrDuma.ExchangeRateApi.model;

import com.github.PiotrDuma.ExchangeRateApi.model.api.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
class ExchangeController {
  private final ExchangeService exchangeService;
}
