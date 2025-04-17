package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
class CurrencyTypeConverter implements AttributeConverter<CurrencyType, String> {
  @Override
  public String convertToDatabaseColumn(CurrencyType attribute) {
    return attribute == null ? null : attribute.name();
  }

  @Override
  public CurrencyType convertToEntityAttribute(String dbData) {
    return dbData == null ? null : CurrencyType.valueOf(dbData);
  }
}
