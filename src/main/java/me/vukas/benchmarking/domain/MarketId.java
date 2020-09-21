package me.vukas.benchmarking.domain;

import lombok.Value;

@Value
public class MarketId {
  private int value;

  public MarketId(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
