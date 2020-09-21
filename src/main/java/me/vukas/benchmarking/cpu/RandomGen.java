package me.vukas.benchmarking.cpu;

import java.util.Random;

public class RandomGen {

  private Random random;

  public RandomGen(long seed){
    this.random = new Random(seed);
  }

  public int getInt(int min, int max){
    return min+random.nextInt(max-min);
  }

  public int getInt(){
    return random.nextInt();
  }

  public double getDouble(){
    return random.nextDouble();
  }

}
