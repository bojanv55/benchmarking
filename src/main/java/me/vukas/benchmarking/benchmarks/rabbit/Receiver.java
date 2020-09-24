package me.vukas.benchmarking.benchmarks.rabbit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  public static CountDownLatch cdlj = new CountDownLatch(1);
  public static CountDownLatch cdlp = new CountDownLatch(1);

  private AtomicInteger remainingJ = new AtomicInteger(RabbitBenchmarks.ITERATIONS);
  private AtomicInteger remainingP = new AtomicInteger(RabbitBenchmarks.ITERATIONS);

  private AtomicLong timeJ = new AtomicLong(0);
  private AtomicLong timeP = new AtomicLong(0);

  @RabbitListener(queues = RabbitConfig.queuej, containerFactory = "prefetchFiveRabbitListenerContainerFactory")
  public void receiveMessagej(byte[] message, @Header("timeSent") long timeSent)
      throws IOException, InterruptedException {
    cdlj.await();

    if(remainingJ.decrementAndGet() > 0){
      long diff = (System.currentTimeMillis()-timeSent);
      timeJ.getAndUpdate(t -> (t==0 ? diff : (t+diff)/2));
    }
    else {
      remainingJ.set(RabbitBenchmarks.ITERATIONS);
      System.out.println("json send/rec. "+RabbitBenchmarks.ITERATIONS+" messages avg millis: " + timeJ);

      try(BufferedWriter writer = new BufferedWriter(new FileWriter("rabbitRec.csv", true))){
        writer.append(String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
            .append(",json,")
            .append(String.valueOf(timeJ))
            .append("\n");
      }

      timeJ = new AtomicLong(0);
      cdlj = new CountDownLatch(1);
    }

  }

  @RabbitListener(queues = RabbitConfig.queuep, containerFactory = "prefetchFiveRabbitListenerContainerFactory")
  public void receiveMessagep(byte[] message, @Header("timeSent") long timeSent)
      throws IOException, InterruptedException {
    cdlp.await();

    if(remainingP.decrementAndGet() > 0){
      long diff = (System.currentTimeMillis()-timeSent);
      timeP.getAndUpdate(t -> (t==0 ? diff : (t+diff)/2));
    }
    else {
      remainingP.set(RabbitBenchmarks.ITERATIONS);
      System.out.println("proto send/rec. "+RabbitBenchmarks.ITERATIONS+" messages avg millis: " + timeP);

      try(BufferedWriter writer = new BufferedWriter(new FileWriter("rabbitRec.csv", true))){
        writer.append(String.valueOf(LocalDateTime.now())) //=DATEVALUE(MID(A1,1,10))+TIMEVALUE(MID(A1,12,8))
            .append(",proto,")
            .append(String.valueOf(timeP))
            .append("\n");
      }

      timeP = new AtomicLong(0);
      cdlp = new CountDownLatch(1);
    }

  }

}
