package me.vukas.benchmarking.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Match {
  private MatchId id;
  private Map<MarketId, Market> markets = new HashMap<>();

  public Match(MatchId id) {
    this.id = id;
  }

  public MatchId getId() {
    return id;
  }

  public void addMarket(Market market){
    this.markets.put(market.getId(), market);
  }

  public Set<Market> getMarkets(){
    return new HashSet<>(markets.values());
  }
}
