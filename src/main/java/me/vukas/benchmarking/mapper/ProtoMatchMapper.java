package me.vukas.benchmarking.mapper;

import java.util.List;
import me.vukas.benchmarking.domain.Market;
import me.vukas.benchmarking.domain.MarketId;
import me.vukas.benchmarking.domain.Match;
import me.vukas.benchmarking.domain.MatchId;
import me.vukas.benchmarking.domain.Outcome;
import me.vukas.benchmarking.domain.OutcomeId;
import me.vukas.benchmarking.dto.MarketDtoOuterClass.MarketDto;
import me.vukas.benchmarking.dto.MarketDtoOuterClass.MarketDto.Builder;
import me.vukas.benchmarking.dto.MatchDtoOuterClass.MatchDto;
import me.vukas.benchmarking.dto.OutcomeDtoOuterClass.OutcomeDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper
public interface ProtoMatchMapper {

  @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
  List<Match> matchesDtoToMatches(List<MatchDto> dtos);

  @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
  List<MatchDto> matchesToMatchesDto(List<Match> matches);

  default MatchDto matchToMatchDto(Match match){
    MatchDto.Builder dto = MatchDto.newBuilder()
        .setId(match.getId().getValue());
    match.getMarkets().forEach(m -> {
      dto.addMarkets(marketToMarketDto(m));
    });
    return dto.build();
  }

  default Match matchDtoToMatch(MatchDto dto){
    MatchId id = new MatchId(dto.getId());
    Match match = new Match(id);
    dto.getMarketsList().forEach(m -> {
      match.addMarket(marketDtoToMarket(m));
    });
    return match;
  }

  default MarketDto marketToMarketDto(Market market){
    Builder dto = MarketDto.newBuilder()
        .setId(market.getId().getValue());
    market.getOutcomes().forEach(o -> {
      dto.addOutcomes(outcomeToOutcomeDto(o));
    });
    return dto.build();
  }

  default Market marketDtoToMarket(MarketDto dto){
    MarketId id = new MarketId(dto.getId());
    Market market = new Market(id);
    dto.getOutcomesList().forEach(o -> {
      market.addOutcome(outcomeDtoToOutcome(o));
    });
    return market;
  }

  default OutcomeDto outcomeToOutcomeDto(Outcome outcome){
    return OutcomeDto.newBuilder()
        .setId(outcome.getId().getValue())
        .setOdds(outcome.getOdds())
        .setProbability(outcome.getProbability())
        .build();
  }

  default Outcome outcomeDtoToOutcome(OutcomeDto dto){
    OutcomeId id = new OutcomeId(dto.getId());
    return new Outcome(id, dto.getOdds(), dto.getProbability());
  }
}
