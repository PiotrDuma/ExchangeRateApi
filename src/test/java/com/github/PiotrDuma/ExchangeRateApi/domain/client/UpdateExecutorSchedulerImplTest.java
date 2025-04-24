package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.PiotrDuma.ExchangeRateApi.api.client.ClientRequestScheduler;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class UpdateExecutorSchedulerImplTest {
  private static final Long INIT_DELAY = 1L;
  private static final Long PERIOD_DELAY = 360L;
  @Mock
  private ClientRequestScheduler taskScheduler;
  @Mock
  private ScheduledExecutorService schedulerMock;

  private UpdateExecutorSchedulerImpl service;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  @BeforeEach
  void setUp(){
    this.service = new UpdateExecutorSchedulerImpl(taskScheduler, clock()){
      @Override
      protected ScheduledExecutorService createScheduler() {
        return schedulerMock;
      }
    };
  }

  @Test
  void shouldInvokeServiceWithValidArguments(){
    doNothing().when(this.taskScheduler).execute();

    this.service.execute();
    this.schedulerMock.shutdownNow();

    verify(this.schedulerMock, times(1))
        .scheduleAtFixedRate(runnableCaptor.capture(), eq(INIT_DELAY), eq(PERIOD_DELAY),
            eq(TimeUnit.MINUTES));

    runnableCaptor.getValue().run();

    verify(this.taskScheduler, times(1)).execute();
  }

  public Clock clock(){
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"), ZoneId.systemDefault());
  }
}