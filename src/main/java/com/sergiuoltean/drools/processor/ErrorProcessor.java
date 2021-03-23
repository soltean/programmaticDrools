package com.sergiuoltean.drools.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    var cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
    var lastEndpointUri = exchange.getProperty(Exchange.TO_ENDPOINT, String.class);
    log.error("Something is wrong with the endpoint {} caused by {}", lastEndpointUri, cause);
  }
}