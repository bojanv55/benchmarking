package me.vukas.benchmarking.domain;

import lombok.Value;

@Value
public class MatchId {
  private int value;

  public MatchId(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
