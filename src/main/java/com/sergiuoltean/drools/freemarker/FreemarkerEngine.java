package com.sergiuoltean.drools.freemarker;

import java.io.StringWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FreemarkerEngine {

  private FreemarkerFactory freemarkerTemplateFactory;

  @SneakyThrows
  public String process(String template, Object model) {
    StringWriter writer = new StringWriter();
    freemarkerTemplateFactory.getTemplate(template).process(model, writer);
    return writer.toString();
  }

}
