package com.github.PiotrDuma.ExchangeRateApi.domain.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.CurrencyType;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.ExchangeService;
import com.github.PiotrDuma.ExchangeRateApi.api.ExchangeRate.dto.ExchangeRateServiceDto;
import com.github.PiotrDuma.ExchangeRateApi.api.client.UpdateExecutorHandler;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
class ClientRequestSchedulerImplTest {
  private static final Long INIT_DELAY = 0L;
  private static final Long PERIOD_DELAY = 10L;
  private static final Long TIMEOUT = 60L;
  @Mock
  private ExchangeService exchangeService;
  @Mock
  private UpdateExecutorHandler updateExecutorHandler;

  @Mock
  private ScheduledExecutorService schedulerMock;

  private ClientRequestSchedulerImpl service;
  @Mock
  private ScheduledFuture<?> scheduledFuture;

  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  @BeforeEach
  void setUp(){
    this.service = new ClientRequestSchedulerImpl(updateExecutorHandler, exchangeService, clock()){
      @Override
      protected ScheduledExecutorService createScheduler(){
        return schedulerMock;
      }
    };
  }

  @Test
  void shouldNotExecuteAnyRequestWhenNoTypeIsReturned(){
    when(this.exchangeService.getAll()).thenReturn(List.of());

    this.service.execute();

    verify(this.updateExecutorHandler, times(0)).updateRates(any());
  }

  @Test
  void shouldInvokeSchedulerExecutionWithValidParameters(){
    ExchangeRateServiceDto dto = getExRateDto(CurrencyType.EUR);

    when(this.exchangeService.getAll()).thenReturn(List.of(dto));

    this.service.execute();

    verify(schedulerMock, times(1)).scheduleWithFixedDelay(
        any(Runnable.class), eq(INIT_DELAY), eq(PERIOD_DELAY), eq(TimeUnit.SECONDS));
    verify(schedulerMock, times(1))
        .schedule(any(Runnable.class), eq(TIMEOUT), eq(TimeUnit.SECONDS));
  }

  @Test
  void shouldExecuteRequestWithValidArgument(){
    CurrencyType base = CurrencyType.EUR;
    ArgumentCaptor<CurrencyType> captor = ArgumentCaptor.forClass(CurrencyType.class);
    ExchangeRateServiceDto dto = getExRateDto(base);

    when(this.exchangeService.getAll()).thenReturn(List.of(dto));
    doNothing().when(this.updateExecutorHandler).updateRates(any());

    this.service.execute();

    verify(schedulerMock, times(1)).scheduleWithFixedDelay(
        runnableCaptor.capture(), eq(INIT_DELAY), eq(PERIOD_DELAY), eq(TimeUnit.SECONDS));
    verify(schedulerMock, times(1))
        .schedule(any(Runnable.class), eq(TIMEOUT), eq(TimeUnit.SECONDS));

    runnableCaptor.getValue().run();

    verify(this.updateExecutorHandler, times(1)).updateRates(captor.capture());
    assertThat(captor.getValue()).isEqualTo(base);
  }

  @Test
  void shouldExecuteRequestsValidNumberOfTimes(){
    ExchangeRateServiceDto dto1 = getExRateDto(CurrencyType.USD);
    ExchangeRateServiceDto dto2 = getExRateDto(CurrencyType.EUR);
    ExchangeRateServiceDto dto3 = getExRateDto(CurrencyType.PLN);

    when(this.exchangeService.getAll()).thenReturn(List.of(dto1, dto2, dto3));
    doNothing().when(this.updateExecutorHandler).updateRates(any());
    doReturn(scheduledFuture).when(schedulerMock).scheduleWithFixedDelay(any(Runnable.class),
        eq(INIT_DELAY), eq(PERIOD_DELAY), eq(TimeUnit.SECONDS));

    this.service.execute();

    verify(schedulerMock, times(3)).scheduleWithFixedDelay(
        runnableCaptor.capture(), eq(INIT_DELAY), eq(PERIOD_DELAY), eq(TimeUnit.SECONDS));
    verify(schedulerMock, times(3))
        .schedule(any(Runnable.class), eq(TIMEOUT), eq(TimeUnit.SECONDS));

    runnableCaptor.getAllValues().forEach(Runnable::run);

    verify(this.updateExecutorHandler, times(3)).updateRates(any());
  }

  @Test
  void shouldTerminateTaskAfterTimeout(){
    ExchangeRateServiceDto dto = getExRateDto(CurrencyType.EUR);

    when(this.exchangeService.getAll()).thenReturn(List.of(dto));

    doReturn(scheduledFuture).when(schedulerMock).scheduleWithFixedDelay(any(Runnable.class),
        eq(INIT_DELAY), eq(PERIOD_DELAY), eq(TimeUnit.SECONDS));
    when(schedulerMock.schedule(any(Runnable.class), eq(TIMEOUT), eq(TimeUnit.SECONDS)))
        .thenAnswer(invocation -> {
          Runnable timeoutTask = invocation.getArgument(0);
          timeoutTask.run();
          return mock(ScheduledFuture.class);
        });

    this.service.execute();
    verify(scheduledFuture).cancel(false);
  }

  private ExchangeRateServiceDto getExRateDto(CurrencyType base){
    return new ExchangeRateServiceDto(base, null,
        null, null, null, null);
  }

  public Clock clock(){
    return Clock.fixed(Instant.parse("2025-10-10T10:15:30.00Z"), ZoneId.systemDefault());
  }
}