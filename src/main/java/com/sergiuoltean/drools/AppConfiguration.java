package com.sergiuoltean.drools;

import com.sergiuoltean.drools.processor.ErrorProcessor;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

  @Bean
  public DeadLetterChannelBuilder deadLetterChannelBuilder(ErrorProcessor errorProcessor) {
    var deadLetterChannelBuilder = new DeadLetterChannelBuilder();
    deadLetterChannelBuilder.setDeadLetterUri("log:error");
    deadLetterChannelBuilder.onExceptionOccurred(errorProcessor)
            .maximumRedeliveries(1)
            .redeliveryDelay(5000);
    return deadLetterChannelBuilder;
  }
}