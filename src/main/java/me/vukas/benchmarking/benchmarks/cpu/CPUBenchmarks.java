package me.vukas.benchmarking.benchmarks.cpu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import me.vukas.benchmarking.benchmarks.DTOGen;
import me.vukas.benchmarking.benchmarks.RandomGen;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class CPUBenchmarks {

  public long seed = 123;
  private static final int ITERATIONS = 300;

  private RandomGen randomGen = new RandomGen(seed);

  public void start() throws IOException {
    System.out.println("Running cpu benchmarks...");

    seed = randomGen.getInt();

    randomGen = new RandomGen(seed);
    StopWatch sw = new StopWatch();
    sw.start("json");
    for(int i=0; i<ITERATIONS; i++){
      new DTOGen(randomGen).mapAndUnmapMatchesJson();
    }
    sw.stop();

    long jsonT = sw.getTotalTimeMillis();
    System.out.println("json: " + jsonT);

    randomGen = new RandomGen(seed);
    sw = new StopWatch();
    sw.start("proto");
    for(int i=0; i<ITERATIONS; i++){
      new DTOGen(randomGen).mapAndUnmapMatchesProto();
    }
    sw.stop();

    long protoT = sw.getTotalTimeMillis();
    System.out.println("proto: " + protoT);

    try(BufferedWriter writer = new BufferedWriter(new FileWriter("cpu.csv", true))){
      writer.append(String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
          .append(",")
          .append(String.valueOf(jsonT))
          .append(",")
          .append(String.valueOf(protoT))
          .append("\n");
    }
  }
}
