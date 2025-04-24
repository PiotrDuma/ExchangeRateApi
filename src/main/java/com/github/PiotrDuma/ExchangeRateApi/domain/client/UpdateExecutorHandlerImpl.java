package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestService;
import com.github.PiotrDuma.ExchangeRateApi.api.client.UpdateExecutorHandler;
import com.github.PiotrDuma.ExchangeRateApi.domain.client.ClientJsonObject.ExchangeRateData;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
class UpdateExecutorHandlerImpl implements UpdateExecutorHandler {
  public static final String NOT_FOUND = "Currency with given type not found.";
  public static final String EXCEPTION = "UpdateExecutorHandlerImpl: ";
  public static final String CLIENT_EXCEPTION = "Client Request execution failed: ";
  public static final String CAST_EXCEPTION = "Json object parse failure.";
  private static final String EXECUTE = "UpdateExecutorHandler: Execute update for: %s";

  private final ClientRequestService requestService;
  private final ExchangeService exchangeService;

  @Override
  public void updateRates(CurrencyType type){
    log.info(String.format(EXECUTE, type));
    try {
      ExchangeRateServiceDto exRate = checkIfExists(type);
      JsonNode json = this.requestService.getExchangeRate(exRate.getBase(),
          exRate.getExchangeCurrencies());
      ExchangeRateData parsed = ExchangeRateData.parse(json);
      parsed.data().rates().remove(type); //filter base currency from rates: base always equals to 1
      this.exchangeService.updateRates(exRate.getBase(), parsed.data().rates());
    }catch (RestClientException ex){
      log.error(EXCEPTION + CLIENT_EXCEPTION);
    }catch (Exception e){
      log.error(EXCEPTION + CAST_EXCEPTION);
    }
  }

  private ExchangeRateServiceDto checkIfExists(CurrencyType type) {
    if(this.exchangeService.getById(type).isEmpty()){
      log.error("UpdateExecutorHandlerImpl: Currency with provided type not found in database.");
      throw new RestClientException(NOT_FOUND);
    }
    return this.exchangeService.getById(type).get();
  }
}
