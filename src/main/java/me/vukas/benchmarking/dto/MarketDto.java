package me.vukas.benchmarking.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class MarketDto {
  private int id;
  private Set<OutcomeDto> outcomes = new HashSet<>();
}
