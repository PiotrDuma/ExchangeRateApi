package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeRateFacade;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "currencies")
class ExchangeRate implements ExchangeRateFacade {
  @Id
  @UuidGenerator
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Version
  @Column(name = "version")
  private Integer version;

  @NotNull
  @Convert(converter = CurrencyTypeConverter.class)
  @Column(name = "base_currency", nullable = false, updatable = false, unique = true)
  private CurrencyType base;
  @ElementCollection(targetClass = CurrencyType.class)
  @Convert(converter = CurrencyTypeConverter.class)
  @CollectionTable(name = "exchange_currencies", joinColumns = { @JoinColumn(name = "currency_id")})
  @Column(name = "type")
  private Set<CurrencyType> exchangeCurrencies;
  @ElementCollection
  @Convert(converter = CurrencyTypeConverter.class, attributeName = "key")
  @MapKeyColumn(name = "currency_type")
  @Column(name = "rate")
  @CollectionTable(name = "rates", joinColumns = {@JoinColumn(name = "currency_id")})
  private Map<CurrencyType, Double> rates;
  private Instant created;
  private Instant updated;
  @PositiveOrZero
  private Double convertedSum;

  ExchangeRate() {
  }

  ExchangeRate(CurrencyType base, Set<CurrencyType> exchangeCurrencies, Clock clock) {
    this.base = base;
    this.exchangeCurrencies = exchangeCurrencies;
    this.rates = new HashMap<>();
    this.created = Instant.now(clock);
    this.updated = created;
    this.convertedSum = 0d;
  }

  public void updateRates(Map<CurrencyType, Double> rates, Clock clock){
    this.rates = rates;
    this.updated = Instant.now(clock);
  }

  public void updateExchangeCurrencies(Set<CurrencyType> exchangeCurrencies, Clock clock){
    this.exchangeCurrencies = exchangeCurrencies;
    this.updated = Instant.now(clock);
  }

  @Override
  public CurrencyType getBase() {
    return this.base;
  }

  @Override
  public Set<CurrencyType> getExchangeCurrencies() {
    return this.exchangeCurrencies;
  }

  @Override
  public Map<CurrencyType, Double> getExchangeRates() {
    return this.rates;
  }

  @Override
  public Instant getCreated() {
    return this.created;
  }

  @Override
  public Instant getLastUpdated() {
    return this.updated;
  }

  @Override
  public Double getConvertedSum() {
    return this.convertedSum;
  }

  protected void setLastUpdated(Instant time){
    this.updated = time;
  }

  protected UUID getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
