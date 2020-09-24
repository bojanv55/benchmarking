package me.vukas.benchmarking.benchmarks.db;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class DBBenchmarks {

  public long seed = 123;
  public static final int ITERATIONS = 200;

  private RandomGen randomGen = new RandomGen(seed);

  private final JdbcTemplate jdbcTemplate;

  public DBBenchmarks(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  static Map<String, Long> len = new HashMap<>();

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

  public void start() throws IOException {
    System.out.println("Running jdbc benchmarks...");

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
      jdbcTemplate.update("INSERT INTO benchmarking_test (data) VALUES (?)",
          (Object) gzipCompress(messages.get(i), "json"));
    }
    sw.stop();

    long jsonT = sw.getTotalTimeMillis();
    System.out.println("json jdbc "+ITERATIONS+": " + jsonT);

    randomGen = new RandomGen(seed);
    sw = new StopWatch();
    messages = new ArrayList<>();
    for(int i=0; i<ITERATIONS; i++){
      byte[] message = new DTOGen(randomGen).generateMatchesWithProtoSerializationByte();
      messages.add(message);
    }
    sw.start("proto");
    for(int i=0; i<ITERATIONS; i++){
      jdbcTemplate.update("INSERT INTO benchmarking_test (data) VALUES (?)",
          (Object) gzipCompress(messages.get(i), "proto"));
    }
    sw.stop();

    long protoT = sw.getTotalTimeMillis();
    System.out.println("proto jdbc "+ITERATIONS+": " + protoT);

    try(BufferedWriter writer = new BufferedWriter(new FileWriter("jdbc.csv", true))){
      writer.append(String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
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

    jdbcTemplate.execute("TRUNCATE TABLE benchmarking_test");

  }

}
