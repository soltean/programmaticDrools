package com.sergiuoltean.drools.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sergiuoltean.drools.ImportProduct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ResultToFileProcessor implements Processor {

  private final ResultProcessor resultProcessor;

  public ResultToFileProcessor(ResultProcessor resultProcessor) {
    this.resultProcessor = resultProcessor;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    final Message message = exchange.getIn();
    byte[] result = getCsvBytes(message.getBody(List.class));
    Files.write(Paths.get("src/main/resources/done/ready.csv"), result);
  }

  private byte[] getCsvBytes(List<ImportProduct> list) throws JsonProcessingException {
    if (!list.isEmpty()) {
      return resultProcessor.process(list);
    } else {
      return "".getBytes();
    }
  }
}