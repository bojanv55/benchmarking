package me.vukas.benchmarking.mapper;

import java.util.List;
import me.vukas.benchmarking.domain.Market;
import me.vukas.benchmarking.domain.MarketId;
import me.vukas.benchmarking.domain.Match;
import me.vukas.benchmarking.domain.MatchId;
import me.vukas.benchmarking.domain.Outcome;
import me.vukas.benchmarking.domain.OutcomeId;
import me.vukas.benchmarking.dto.MarketDto;
import me.vukas.benchmarking.dto.MatchDto;
import me.vukas.benchmarking.dto.OutcomeDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper
public interface MatchMapper {

  @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
  List<Match> matchesDtoToMatches(List<MatchDto> dtos);

  @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
  List<MatchDto> matchesToMatchesDto(List<Match> matches);

  default MatchDto matchToMatchDto(Match match){
    MatchDto dto = new MatchDto();
    dto.setId(match.getId().getValue());
    match.getMarkets().forEach(m -> {
      dto.getMarkets().add(marketToMarketDto(m));
    });
    return dto;
  }

  default Match matchDtoToMatch(MatchDto dto){
    MatchId id = new MatchId(dto.getId());
    Match match = new Match(id);
    dto.getMarkets().forEach(m -> {
      match.addMarket(marketDtoToMarket(m));
    });
    return match;
  }

  default MarketDto marketToMarketDto(Market market){
    MarketDto dto = new MarketDto();
    dto.setId(market.getId().getValue());
    market.getOutcomes().forEach(o -> {
      dto.getOutcomes().add(outcomeToOutcomeDto(o));
    });
    return dto;
  }

  default Market marketDtoToMarket(MarketDto dto){
    MarketId id = new MarketId(dto.getId());
    Market market = new Market(id);
    dto.getOutcomes().forEach(o -> {
      market.addOutcome(outcomeDtoToOutcome(o));
    });
    return market;
  }

  default OutcomeDto outcomeToOutcomeDto(Outcome outcome){
    OutcomeDto dto = new OutcomeDto();
    dto.setId(outcome.getId().getValue());
    dto.setOdds(outcome.getOdds());
    dto.setProbability(outcome.getProbability());
    return dto;
  }

  default Outcome outcomeDtoToOutcome(OutcomeDto dto){
    OutcomeId id = new OutcomeId(dto.getId());
    return new Outcome(id, dto.getOdds(), dto.getProbability());
  }

}
