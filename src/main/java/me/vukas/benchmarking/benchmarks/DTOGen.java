package me.vukas.benchmarking.benchmarks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.vukas.benchmarking.domain.Match;
import me.vukas.benchmarking.dto.MarketDto;
import me.vukas.benchmarking.dto.MarketDtoOuterClass;
import me.vukas.benchmarking.dto.MatchDto;
import me.vukas.benchmarking.dto.MatchDtoOuterClass;
import me.vukas.benchmarking.dto.MatchDtosOuterClass.MatchDtos;
import me.vukas.benchmarking.dto.MatchDtosOuterClass.MatchDtos.Builder;
import me.vukas.benchmarking.dto.OutcomeDto;
import me.vukas.benchmarking.dto.OutcomeDtoOuterClass;
import me.vukas.benchmarking.mapper.MatchMapper;
import me.vukas.benchmarking.mapper.MatchMapperImpl;
import me.vukas.benchmarking.mapper.ProtoMatchMapper;
import me.vukas.benchmarking.mapper.ProtoMatchMapperImpl;

public class DTOGen {

  public int numberOfMatches = 123;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final MatchMapper matchMapper = new MatchMapperImpl();
  private final ProtoMatchMapper protoMatchMapper = new ProtoMatchMapperImpl();
  private final RandomGen randomGen;

  public DTOGen(RandomGen randomGen) {
    this.randomGen = randomGen;
  }

  public void mapAndUnmapMatchesJson() throws IOException {
    List<MatchDto> matchDtos = generateMatchesWithJsonSerialization();
    List<Match> matches = matchMapper.matchesDtoToMatches(matchDtos);
    Object x = matchMapper.matchesToMatchesDto(matches);
  }

  public List<MatchDto> generateMatchesWithJsonSerialization()
      throws IOException {
    List<MatchDto> matchDtos = generateMatches(numberOfMatches);
    byte[] matchDtosBytes = objectMapper.writeValueAsBytes(matchDtos);
    matchDtos = objectMapper.readValue(matchDtosBytes, new TypeReference<List<MatchDto>>() {});
    return matchDtos;
  }

  public void mapAndUnmapMatchesProto() throws IOException {
    MatchDtos matchDtos = generateMatchesWithProtoSerialization();
    List<Match> matches = protoMatchMapper.matchesDtoToMatches(matchDtos.getMatchesList());
    Object x = protoMatchMapper.matchesToMatchesDto(matches);
  }

  public MatchDtos generateMatchesWithProtoSerialization()
      throws IOException {
    MatchDtos matchDtos = generateMatchesProto(numberOfMatches);
    byte[] matchDtosBytes = matchDtos.toByteArray();
    matchDtos = MatchDtos.parseFrom(matchDtosBytes);
    return matchDtos;
  }

  public byte[] generateMatchesWithProtoSerializationByte()
      throws IOException {
    MatchDtos matchDtos = generateMatchesProto(numberOfMatches);
    return matchDtos.toByteArray();
  }

  public byte[] generateMatchesWithJsonSerializationByte()
      throws IOException {
    List<MatchDto> matchDtos = generateMatches(numberOfMatches);
    return objectMapper.writeValueAsBytes(matchDtos);
  }

  public List<MatchDto> generateMatches(int numberOfMatches){
    List<MatchDto> matches = new ArrayList<>();
    for(int i=0; i<numberOfMatches; i++){
      matches.add(generateMatchDto());
    }
    return matches;
  }

  public MatchDtos generateMatchesProto(int numberOfMatches){
    Builder matches = MatchDtos.newBuilder();
    for(int i=0; i<numberOfMatches; i++){
      matches.addMatches(generateMatchDtoProto());
    }
    return matches.build();
  }

  public MatchDto generateMatchDto(){
    int nrMarkets = randomGen.getInt(10,40);
    MatchDto dto = new MatchDto();
    dto.setId(randomGen.getInt());
    for(int i=0; i<nrMarkets; i++){
      dto.getMarkets().add(generateMarketDto());
    }
    return dto;
  }

  public MatchDtoOuterClass.MatchDto generateMatchDtoProto(){
    int nrMarkets = randomGen.getInt(10,40);
    MatchDtoOuterClass.MatchDto.Builder dto = MatchDtoOuterClass.MatchDto.newBuilder()
        .setId(randomGen.getInt());
    for(int i=0; i<nrMarkets; i++){
      dto.addMarkets(generateMarketDtoProto());
    }
    return dto.build();
  }

  public MarketDto generateMarketDto(){
    int nrOutcomes = randomGen.getInt(2,20);
    MarketDto dto = new MarketDto();
    dto.setId(randomGen.getInt(1,500));
    for(int i=0; i<nrOutcomes; i++){
      dto.getOutcomes().add(generateOutcomeDto());
    }
    return dto;
  }

  public MarketDtoOuterClass.MarketDto generateMarketDtoProto(){
    int nrOutcomes = randomGen.getInt(2,20);
    MarketDtoOuterClass.MarketDto.Builder dto = MarketDtoOuterClass.MarketDto.newBuilder()
        .setId(randomGen.getInt(1,500));
    for(int i=0; i<nrOutcomes; i++){
      dto.addOutcomes(generateOutcomeDtoProto());
    }
    return dto.build();
  }

  public OutcomeDto generateOutcomeDto(){
    OutcomeDto dto = new OutcomeDto();
    dto.setId(randomGen.getInt(1,1000));
    dto.setOdds(randomGen.getDouble());
    dto.setProbability(randomGen.getDouble());
    return dto;
  }

  public OutcomeDtoOuterClass.OutcomeDto generateOutcomeDtoProto(){
    return OutcomeDtoOuterClass.OutcomeDto.newBuilder()
        .setId(randomGen.getInt(1,1000))
        .setOdds(randomGen.getDouble())
        .setProbability(randomGen.getDouble())
        .build();
  }

}
