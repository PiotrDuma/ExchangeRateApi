package com.github.PiotrDuma.ExchangeRateApi.infrastructure.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Clock clock;

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request){

    HttpServletRequest httpRequest = getHttpServletRequest(request);
    List<Map<String, String>> messages = ex.getFieldErrors().stream()
        .map( e -> {
          HashMap<String, String> tmp = new HashMap<>();
          tmp.put(e.getField(), e.getDefaultMessage());
          return tmp;
        })
        .collect(Collectors.toList());

    ExceptionDto body = new ExceptionDto(
        LocalDateTime.now(clock),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        HttpStatus.BAD_REQUEST.value(),
        httpRequest.getRequestURI(),
        messages
    );

    return new ResponseEntity<>(body, headers, status);
  }

  private HttpServletRequest getHttpServletRequest(WebRequest request) {
    return ((ServletWebRequest) request).getRequest();
  }

  private record ExceptionDto(
      LocalDateTime timestamp,
      String title,
      int status,
      String instance,
      List<Map<String, String>> details
  ) {
  }
}
