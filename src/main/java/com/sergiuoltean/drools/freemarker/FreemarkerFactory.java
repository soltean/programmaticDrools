package com.sergiuoltean.drools.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@CacheConfig(cacheNames = "templates")
@Component
public class FreemarkerFactory {

  private Configuration configuration;

  @PostConstruct
  private void loadConfiguration() {
    configuration = new Configuration(Configuration.VERSION_2_3_28);
    configuration.setClassicCompatible(true);
    configuration.setClassForTemplateLoading(this.getClass(), "/templates");
  }

  @Cacheable
  @SneakyThrows
  public Template getTemplate(String template) {
    return configuration.getTemplate(template);
  }

}
