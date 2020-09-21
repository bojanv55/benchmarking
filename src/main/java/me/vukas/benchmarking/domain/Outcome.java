package me.vukas.benchmarking.domain;

public class Outcome {
  private OutcomeId id;
  private double odds;
  private double probability;

  public Outcome(OutcomeId id, double odds, double probability) {
    this.id = id;
    this.odds = odds;
    this.probability = probability;
  }

  public OutcomeId getId() {
    return id;
  }

  public double getOdds() {
    return odds;
  }

  public double getProbability() {
    return probability;
  }
}
