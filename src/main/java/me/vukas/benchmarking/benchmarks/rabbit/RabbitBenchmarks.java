package me.vukas.benchmarking.benchmarks.rabbit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import me.vukas.benchmarking.benchmarks.DTOGen;
import me.vukas.benchmarking.benchmarks.RandomGen;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class RabbitBenchmarks {

  public long seed = 123;
  public static final int ITERATIONS = 200;

  private RandomGen randomGen = new RandomGen(seed);

  private final RabbitTemplate rabbitTemplate;

  public RabbitBenchmarks(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void start() throws IOException {
    System.out.println("Running rabbit benchmarks...");

    seed = randomGen.getInt();

    randomGen = new RandomGen(seed);
    StopWatch sw = new StopWatch();
    List<byte[]> messages = new ArrayList<>();
    for(int i=0; i<ITERATIONS; i++){
      byte[] message = new DTOGen(randomGen).generateMatchesWithJsonSerializationByte();
      messages.add(message);
    }
    sw.start("json");
    for(int i=0; i<ITERATIONS; i++){
      rabbitTemplate.convertAndSend(RabbitConfig.queuej, messages.get(i), m -> {
        m.getMessageProperties().getHeaders().put("timeSent", System.currentTimeMillis());
        return m;
      });
    }
    sw.stop();

    long jsonT = sw.getTotalTimeMillis();
    System.out.println("json send "+ITERATIONS+": " + jsonT);

    Receiver.cdlj.countDown();

    randomGen = new RandomGen(seed);
    sw = new StopWatch();
    messages = new ArrayList<>();
    for(int i=0; i<ITERATIONS; i++){
      byte[] message = new DTOGen(randomGen).generateMatchesWithProtoSerializationByte();
      messages.add(message);
    }
    sw.start("proto");
    for(int i=0; i<ITERATIONS; i++){
      rabbitTemplate.convertAndSend(RabbitConfig.queuep, messages.get(i), m -> {
        m.getMessageProperties().getHeaders().put("timeSent", System.currentTimeMillis());
        return m;
      });
    }
    sw.stop();

    long protoT = sw.getTotalTimeMillis();
    System.out.println("proto send "+ITERATIONS+": " + protoT);

    try(BufferedWriter writer = new BufferedWriter(new FileWriter("rabbit.csv", true))){
      writer.append(String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
          .append(",")
          .append(String.valueOf(jsonT))
          .append(",")
          .append(String.valueOf(protoT))
          .append("\n");
    }

    Receiver.cdlp.countDown();

  }

}
