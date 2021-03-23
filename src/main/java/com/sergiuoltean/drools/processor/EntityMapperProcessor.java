package com.sergiuoltean.drools.processor;

import com.sergiuoltean.drools.ImportProduct;
import com.sergiuoltean.drools.RulesAndFacts;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class EntityMapperProcessor implements Processor {

  private Map<Integer, String> translationIndexes;

  @Override
  public void process(Exchange exchange) throws Exception {
    List<List<String>> data = (List<List<String>>) exchange.getIn().getBody();
    List<ImportProduct> menuItems = data.stream().map(this::mapImportProducts).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(menuItems)) {
      RulesAndFacts<ImportProduct> rulesAndFacts = new RulesAndFacts(menuItems);
      exchange.getIn().setBody(rulesAndFacts);
    } else {
      log.info("Nothing to process");
      //todo: handle better
      throw new RuntimeException("Do not leave me like this");
    }
  }

  private ImportProduct mapImportProducts(List<String> line) {
    ImportProduct importProduct = new ImportProduct();
    importProduct.setCategory(line.get(0));
    importProduct.setId(line.get(1));
    importProduct.setStatus(line.get(2));
    importProduct.setPrice(Double.valueOf(line.get(3)));
    return importProduct;
  }

}
