package com.github.PiotrDuma.ExchangeRateApi.client;

import com.github.PiotrDuma.ExchangeRateApi.domain.api.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.domain.api.ExchangeRateDTO;
import java.util.List;

public interface ClientRequestService {
  ExchangeRateDTO getExchangeRate(CurrencyType base, List<CurrencyType> target);
}
