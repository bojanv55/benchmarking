package me.vukas.benchmarking.benchmarks;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import me.vukas.benchmarking.benchmarks.cpu.CPUBenchmarks;
import me.vukas.benchmarking.benchmarks.db.DBBenchmarks;
import me.vukas.benchmarking.benchmarks.rabbit.RabbitBenchmarks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledAll {

  private CPUBenchmarks cpuBenchmarks;
  private RabbitBenchmarks rabbitBenchmarks;
  private DBBenchmarks dbBenchmarks;
//  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Scheduled(fixedDelay = 60_000, initialDelay = 10_000)
  public void startAll() throws IOException {

//    RandomGen gen = new RandomGen(123);
//
//    for(int i=0; i<100; i++) {
//      List<Integer> ids = gen.randomList(4, 100);
//
//      Map<String, List> paramMap = Collections.singletonMap("nesto", ids);
//      namedParameterJdbcTemplate.query("SELECT * from benchmarking_test where id in (:nesto)",
//          paramMap, new RowMapper<Object>() {
//            @Override
//            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
//              return null;
//            }
//          });
//    }

    cpuBenchmarks.start();
    rabbitBenchmarks.start();
    dbBenchmarks.start();
  }

}
