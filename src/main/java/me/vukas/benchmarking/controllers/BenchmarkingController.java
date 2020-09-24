package me.vukas.benchmarking.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BenchmarkingController {

  private static boolean done = false;

  @PostMapping("/cpu")
  public void startSerializationBenchmarks() {



  }

}
