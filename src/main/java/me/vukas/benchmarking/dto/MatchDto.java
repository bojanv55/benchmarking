package me.vukas.benchmarking.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class MatchDto {
  private int id;
  private Set<MarketDto> markets = new HashSet<>();
}
