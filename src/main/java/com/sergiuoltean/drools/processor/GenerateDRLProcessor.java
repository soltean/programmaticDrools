package com.sergiuoltean.drools.processor;

import com.sergiuoltean.drools.ImportProduct;
import com.sergiuoltean.drools.RulesAndFacts;
import com.sergiuoltean.drools.freemarker.FreemarkerEngine;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDRLProcessor implements Processor {

  private final FreemarkerEngine freemarkerEngine;
  private static final String DELETE_TEMPLATE = "delete.ftl";

  @Autowired
  public GenerateDRLProcessor(FreemarkerEngine freemarkerEngine) {
    this.freemarkerEngine = freemarkerEngine;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    RulesAndFacts<ImportProduct> rulesAndFacts = exchange.getIn().getBody(RulesAndFacts.class);
    List<Double> deleteData = rulesAndFacts.getDeleteRuleData();
    List<String> deleteRules = deleteData.stream().map(del ->
            freemarkerEngine.process(DELETE_TEMPLATE, Map.of("productId", del, "idx", deleteData.indexOf(del)))
    ).collect(Collectors.toList());
    rulesAndFacts.setRules(deleteRules);
  }
}
