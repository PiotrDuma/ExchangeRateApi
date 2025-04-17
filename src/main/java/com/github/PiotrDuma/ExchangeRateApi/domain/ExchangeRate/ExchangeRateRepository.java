package com.github.PiotrDuma.ExchangeRateApi.domain.ExchangeRate;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ExchangeRateRepository extends JpaRepository<ExchangeRate, UUID> {

}
