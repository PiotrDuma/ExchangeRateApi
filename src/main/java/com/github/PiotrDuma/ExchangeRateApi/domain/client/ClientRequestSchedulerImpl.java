package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestScheduler;
import com.github.PiotrDuma.ExchangeRateApi.api.client.UpdateExecutorHandler;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ClientRequestSchedulerImpl implements ClientRequestScheduler {
  public static final String EXCEPTION = "SingleRequestScheduler: %s: Request execution failure. ";
  public static final String EXCEPTION_TIMEOUT = "SingleRequestScheduler: %s: Request execution timeout. ";
  private static final Long INIT_DELAY = 0L;
  private static final int THREADS = 1; //number of request is limited by external service
  private static final Long PERIOD_DELAY = 10L; //number of request per minute is also limited
  private static final Long TIMEOUT = 60L;


  private final ScheduledExecutorService scheduler;
  private final UpdateExecutorHandler updateExecutor;
  private final ExchangeService exchangeService;
  private final Clock clock;

  @Autowired
  public ClientRequestSchedulerImpl(UpdateExecutorHandler updateExecutor,
      ExchangeService exchangeService, Clock clock) {
    this.scheduler = createScheduler();
    this.updateExecutor = updateExecutor;
    this.exchangeService = exchangeService;
    this.clock = clock;
  }

  @Override
  public void execute() {
    List<CurrencyType> types = getListOfTypesToUpdate();

    try{
      for (CurrencyType type : types) {
        ScheduledFuture<?> request = scheduler.scheduleWithFixedDelay(
            () -> executeTask(type), INIT_DELAY, PERIOD_DELAY, TimeUnit.SECONDS);

        scheduler.schedule(() -> terminateTask(request), TIMEOUT, TimeUnit.SECONDS);
      }
    }catch (Exception e){
      log.error(String.format(EXCEPTION, this.clock.instant()) + e.getMessage());
    }finally {
      shutdown();
    }
  }

  protected ScheduledExecutorService createScheduler() {
    return Executors.newScheduledThreadPool(THREADS);
  }

  private void executeTask(CurrencyType type){
    this.updateExecutor.updateRates(type);
  }

  private void terminateTask(ScheduledFuture<?> request){
    log.warn(String.format(EXCEPTION_TIMEOUT, this.clock.instant()));
    request.cancel(false);
  }

  private List<CurrencyType> getListOfTypesToUpdate(){
    return this.exchangeService.getAll().stream()
        .map(ExchangeRateServiceDto::getBase)
        .toList();
  }

  private void shutdown() {
    if (scheduler != null && !scheduler.isShutdown()) {
      scheduler.shutdown();
      try {
        if (!scheduler.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
          scheduler.shutdownNow();
        }
      } catch (InterruptedException e) {
        scheduler.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
  }
}
