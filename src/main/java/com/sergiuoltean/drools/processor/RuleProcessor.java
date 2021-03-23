package com.sergiuoltean.drools.processor;

import com.sergiuoltean.drools.ImportProduct;
import com.sergiuoltean.drools.KieService;
import com.sergiuoltean.drools.RulesAndFacts;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RuleProcessor implements Processor {

  private final KieService<ImportProduct> kieService;

  @Autowired
  public RuleProcessor(KieService<ImportProduct> kieService) {
    this.kieService = kieService;
    this.kieService.setResultClass(ImportProduct.class);
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    RulesAndFacts<ImportProduct> rulesAndFacts = exchange.getIn().getBody(RulesAndFacts.class);
    List<? super Object> facts = new ArrayList<>(rulesAndFacts.getFacts());
    exchange.getIn().setBody(kieService.executeCommands(rulesAndFacts.getRules(), facts));
  }
}
