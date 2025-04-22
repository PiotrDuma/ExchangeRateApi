package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestService;
import com.github.PiotrDuma.ExchangeRateApi.api.client.UpdateExecutorHandler;
import com.github.PiotrDuma.ExchangeRateApi.domain.client.ClientJsonObject.ExchangeRateData;
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

  private final ClientRequestService requestService;
  private final ExchangeService exchangeService;

  @Override
  public void updateRates(CurrencyType type) throws RestClientException{
    ExchangeRateServiceDto exRate = checkIfExists(type);

    try {
      JsonNode json = this.requestService.getExchangeRate(exRate.getBase(),
          exRate.getExchangeCurrencies());
      ExchangeRateData parsed = ExchangeRateData.parse(json);
      this.exchangeService.updateRates(exRate.getBase(), parsed.data().rates());
    }catch (RestClientException ex){
      log.error(EXCEPTION + CLIENT_EXCEPTION);
    }catch (IllegalArgumentException e){
      log.error(EXCEPTION + CAST_EXCEPTION);
    }catch(Exception e){
      throw new RestClientException(EXCEPTION + e.getMessage());//propagate exception
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
