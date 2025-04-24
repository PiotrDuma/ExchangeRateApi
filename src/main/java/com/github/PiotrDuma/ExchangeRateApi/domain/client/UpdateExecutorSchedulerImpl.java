package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestScheduler;
import com.github.PiotrDuma.ExchangeRateApi.api.client.UpdateExecutorScheduler;
import java.time.Clock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class UpdateExecutorSchedulerImpl implements UpdateExecutorScheduler {
  private static final String EXECUTE = "UpdateExecutorScheduler: Invoke update task: %s: ";
  public static final String EXCEPTION = "SingleRequestScheduler: %s: Request execution failure. ";
  private static final Long INIT_DELAY = 1L; //minutes
  private static final Long PERIOD_DELAY = 360L; //Limit number of invokes to 4 times a day.
  private final ScheduledExecutorService scheduler;
  private final ClientRequestScheduler taskScheduler;
  private final Clock clock;

  @Autowired
  public UpdateExecutorSchedulerImpl(ClientRequestScheduler taskScheduler, Clock clock) {
    this.scheduler = createScheduler();
    this.taskScheduler = taskScheduler;
    this.clock = clock;
  }

  @EventListener(ApplicationReadyEvent.class)
  private void invoke(){
    execute();
  }

  @Override
  public void execute() {
    try{
      scheduler.scheduleAtFixedRate(this::executeTask, INIT_DELAY, PERIOD_DELAY, TimeUnit.MINUTES);
    }catch (Exception e){
      log.error(String.format(EXCEPTION, this.clock.instant()));
      log.error(e.getMessage());
    }
  }

  protected ScheduledExecutorService createScheduler() {
    return Executors.newSingleThreadScheduledExecutor();
  }

  private void executeTask(){
    log.info(String.format(EXECUTE, this.clock.instant()));
    this.taskScheduler.execute();
  }
}
