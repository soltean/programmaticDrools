package com.sergiuoltean.drools;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KieService<T> {

  protected static final String ENCODING = "UTF-8";
  private Class resultClass;

  public KieContainer kieContainer(List<String> dynamicDrls) {
    KieServices kieServices = KieServices.Factory.get();

    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    kieFileSystem.write(ResourceFactory.newClassPathResource("rules/initial.drl", ENCODING));
    dynamicDrls.forEach(drl -> kieFileSystem.write("src/main/resources/" + UUID.randomUUID().toString() + ".drl",
            ResourceFactory.newReaderResource(new StringReader(drl), ENCODING)));
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    kieBuilder.buildAll();
    KieModule kieModule = kieBuilder.getKieModule();
    return kieServices.newKieContainer(kieModule.getReleaseId());
  }

  public void setResultClass(Class resultClass) {
    this.resultClass = resultClass;
  }

  public List<T> executeCommands(List<String> dynamicDrls, List<?> facts) {
    KieServices kieServices = KieServices.Factory.get();
    KieBaseConfiguration kieBaseConf = kieServices.newKieBaseConfiguration();
    KieSession kieSession = kieContainer(dynamicDrls).newKieBase(kieBaseConf).newKieSession();
    try {
      String outIdentifier = "out";
      Command<?> batchCommand = prepareCommands(facts, outIdentifier);
      ExecutionResults result = (ExecutionResults) kieSession.execute(batchCommand);
      return (List<T>) result.getValue(outIdentifier);
    } finally {
      kieSession.dispose();
    }
  }

  private Command<?> prepareCommands(List<?> facts, String outIdentifier) {
    KieCommands commandsFactory = KieServices.Factory.get().getCommands();
    List<Command> commands = facts.stream().map(commandsFactory::newInsert).collect(Collectors.toList());
    commands.add(commandsFactory.newFireAllRules());
    ObjectFilter factsFilter = new ClassObjectFilter(resultClass);
    commands.add(commandsFactory.newGetObjects(factsFilter, outIdentifier));
    return commandsFactory.newBatchExecution(commands);
  }

}
