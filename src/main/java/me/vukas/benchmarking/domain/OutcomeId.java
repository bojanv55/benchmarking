package me.vukas.benchmarking.domain;

import lombok.Value;

@Value
public class OutcomeId {
  private int value;

  public OutcomeId(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
