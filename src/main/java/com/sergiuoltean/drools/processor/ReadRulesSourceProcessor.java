package com.sergiuoltean.drools.processor;

import com.sergiuoltean.drools.RulesAndFacts;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadRulesSourceProcessor implements Processor {

  protected static final String DYNAMIC_RULE_FILE = "importProduct.xlsx";

  @Override
  public void process(Exchange exchange) throws Exception {
    RulesAndFacts rulesAndFacts = exchange.getIn().getBody(RulesAndFacts.class);
    InputStream dynamicRules1 = getClass().getResourceAsStream("/dynamicRules/" + DYNAMIC_RULE_FILE);
    rulesAndFacts.setDynamicRulesStream(dynamicRules1);
  }
}
