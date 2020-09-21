package me.vukas.benchmarking.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Market {
  private MarketId id;
  private Map<OutcomeId, Outcome> outcomes = new HashMap<>();

  public Market(MarketId id) {
    this.id = id;
  }

  public MarketId getId() {
    return id;
  }

  public void addOutcome(Outcome outcome){
    this.outcomes.put(outcome.getId(), outcome);
  }

  public Set<Outcome> getOutcomes(){
    return new HashSet<>(outcomes.values());
  }
}
