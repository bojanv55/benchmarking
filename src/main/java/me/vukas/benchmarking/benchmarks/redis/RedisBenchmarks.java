package me.vukas.benchmarking.benchmarks.redis;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import me.vukas.benchmarking.benchmarks.DTOGen;
import me.vukas.benchmarking.benchmarks.RandomGen;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class RedisBenchmarks {

  private RedisTemplate<Integer, byte[]> byteTemplate1;
  private RedisTemplate<Integer, byte[]> byteTemplate2;

  public long seed = 123;
  public static final int ITERATIONS = 200;

  private RandomGen randomGen = new RandomGen(seed);

  static Map<String, Long> len = new HashMap<>();

  public RedisBenchmarks(
      RedisTemplate<Integer, byte[]> byteTemplate1,
      RedisTemplate<Integer, byte[]> byteTemplate2) {
    this.byteTemplate1 = byteTemplate1;
    this.byteTemplate2 = byteTemplate2;
  }

  private static byte[] gzipCompress(byte[] uncompressedData, String tech) {
    StopWatch sw = new StopWatch();
    sw.start();
    byte[] result = new byte[]{};
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
        GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
      gzipOS.write(uncompressedData);
      // You need to close it before using bos
      gzipOS.close();
      result = bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    sw.stop();
    len.put(tech, len.getOrDefault(tech, 0L) + sw.getTotalTimeMillis());
    return result;
  }

  private static byte[] gzipUncompress(byte[] compressedData) {
    byte[] result = new byte[]{};
    try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipIS.read(buffer)) != -1) {
        bos.write(buffer, 0, len);
      }
      result = bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void start1() throws IOException {

    System.out.println("Deleting before redis benchmarks...");
    for (int i = 0; i < ITERATIONS * 2; i++) {
      byteTemplate1.delete(i);
    }

    System.out.println("Running redis benchmarks...");

    seed = randomGen.getInt();

    randomGen = new RandomGen(seed);
    StopWatch sw = new StopWatch();
    List<byte[]> messages = new ArrayList<>();
    for (int i = 0; i < ITERATIONS; i++) {
      byte[] message = new DTOGen(randomGen).generateMatchesWithJsonSerializationByte();
      messages.add(gzipCompress(message, "json"));
    }
    sw.start("json");
    for (int i = 0; i < ITERATIONS; i++) {
      byteTemplate1.opsForValue().set(i, messages.get(i));
    }
    sw.stop();

    long jsonT = sw.getTotalTimeMillis();
    System.out.println("json redis1 " + ITERATIONS + ": " + jsonT);

    randomGen = new RandomGen(seed);
    sw = new StopWatch();
    messages = new ArrayList<>();
    for (int i = 0; i < ITERATIONS; i++) {
      byte[] message = new DTOGen(randomGen).generateMatchesWithProtoSerializationByte();
      messages.add(gzipCompress(message, "proto"));
    }
    sw.start("proto");
    for (int i = 0; i < ITERATIONS; i++) {
      byteTemplate1.opsForValue().set(ITERATIONS + i, messages.get(i));
    }
    sw.stop();

    long protoT = sw.getTotalTimeMillis();
    System.out.println("proto redis1 " + ITERATIONS + ": " + protoT);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("redis1.csv", true))) {
      writer.append(
          String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
          .append(",")
          .append(String.valueOf(jsonT))
          .append(",")
          .append(String.valueOf(protoT))
          .append(",")
          .append(String.valueOf(len.get("json")))
          .append(",")
          .append(String.valueOf(len.get("proto")))
          .append("\n");
    }

    System.out.println("TIME TO COMPRESS" + len);
    len = new HashMap<>();

    System.out.println("Deleting after redis benchmarks...");
    for (int i = 0; i < ITERATIONS * 2; i++) {
      byteTemplate1.delete(i);
    }

  }

  public void start2() throws IOException {

    System.out.println("Deleting before redis benchmarks...");
    for (int i = 0; i < ITERATIONS * 2; i++) {
      byteTemplate2.delete(i);
    }

    System.out.println("Running redis benchmarks...");

    seed = randomGen.getInt();

    randomGen = new RandomGen(seed);
    StopWatch sw = new StopWatch();
    List<byte[]> messages = new ArrayList<>();
    for (int i = 0; i < ITERATIONS; i++) {
      byte[] message = new DTOGen(randomGen).generateMatchesWithJsonSerializationByte();
      messages.add(gzipCompress(message, "json"));
    }
    sw.start("json");
    for (int i = 0; i < ITERATIONS; i++) {
      byteTemplate2.opsForValue().set(i, messages.get(i));
    }
    sw.stop();

    long jsonT = sw.getTotalTimeMillis();
    System.out.println("json redis2 " + ITERATIONS + ": " + jsonT);

    randomGen = new RandomGen(seed);
    sw = new StopWatch();
    messages = new ArrayList<>();
    for (int i = 0; i < ITERATIONS; i++) {
      byte[] message = new DTOGen(randomGen).generateMatchesWithProtoSerializationByte();
      messages.add(gzipCompress(message, "proto"));
    }
    sw.start("proto");
    for (int i = 0; i < ITERATIONS; i++) {
      byteTemplate2.opsForValue().set(ITERATIONS + i, messages.get(i));
    }
    sw.stop();

    long protoT = sw.getTotalTimeMillis();
    System.out.println("proto redis2 " + ITERATIONS + ": " + protoT);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("redis2.csv", true))) {
      writer.append(
          String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
          .append(",")
          .append(String.valueOf(jsonT))
          .append(",")
          .append(String.valueOf(protoT))
          .append(",")
          .append(String.valueOf(len.get("json")))
          .append(",")
          .append(String.valueOf(len.get("proto")))
          .append("\n");
    }

    System.out.println("TIME TO COMPRESS" + len);
    len = new HashMap<>();

    System.out.println("Deleting after redis benchmarks...");
    for (int i = 0; i < ITERATIONS * 2; i++) {
      byteTemplate2.delete(i);
    }

  }

}
