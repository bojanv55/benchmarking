package me.vukas.benchmarking;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BenchmarkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BenchmarkingApplication.class, args);
	}

}
