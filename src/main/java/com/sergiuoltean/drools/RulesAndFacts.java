package com.sergiuoltean.drools;

import java.io.InputStream;
import java.util.List;
import lombok.Data;

@Data
public class RulesAndFacts<T> {

  private List<T> facts;
  private List<String> rules;
  private InputStream dynamicRulesStream;
  private List<Double> deleteRuleData;

  public RulesAndFacts(List<T> facts) {
    this.facts = facts;
  }

}
