package com.sergiuoltean.drools;

import com.sergiuoltean.drools.processor.EntityMapperProcessor;
import com.sergiuoltean.drools.processor.ErrorProcessor;
import com.sergiuoltean.drools.processor.ExcelProcessor;
import com.sergiuoltean.drools.processor.GenerateDRLProcessor;
import com.sergiuoltean.drools.processor.ReadRulesSourceProcessor;
import com.sergiuoltean.drools.processor.ResultProcessor;
import com.sergiuoltean.drools.processor.ResultToFileProcessor;
import com.sergiuoltean.drools.processor.RuleProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainRoute extends RouteBuilder {

  private final EntityMapperProcessor entityMapperProcessor;
  private final ExcelProcessor excelProcessor;
  private final GenerateDRLProcessor generateDRLProcessor;
  private final ReadRulesSourceProcessor readRulesSourceProcessor;
  private final ResultToFileProcessor resultToFile;
  private final ErrorProcessor errorProcessor;
  private final RuleProcessor ruleProcessor;

  //fixme: usually I build more than one route to break complexity
  public MainRoute(EntityMapperProcessor entityMapperProcessor,
          ExcelProcessor excelProcessor, GenerateDRLProcessor generateDRLProcessor,
          ReadRulesSourceProcessor readRulesSourceProcessor,
          ResultToFileProcessor resultToFile, ResultProcessor resultProcessor,
          ErrorProcessor errorProcessor, RuleProcessor ruleProcessor) {
    this.entityMapperProcessor = entityMapperProcessor;
    this.excelProcessor = excelProcessor;
    this.generateDRLProcessor = generateDRLProcessor;
    this.readRulesSourceProcessor = readRulesSourceProcessor;
    this.resultToFile = resultToFile;
    this.errorProcessor = errorProcessor;
    this.ruleProcessor = ruleProcessor;
  }

  @Override
  public void configure() {
    errorHandler(deadLetterChannel("log:error").onExceptionOccurred(errorProcessor).maximumRedeliveries(1));
    from("file:src/main/resources/poll?noop=true")
            .unmarshal().csv()
            .process(entityMapperProcessor)
            .process(readRulesSourceProcessor)
            .process(excelProcessor)
            .process(generateDRLProcessor)
            .process(ruleProcessor)
            .process(resultToFile)
            .end();

  }
}
