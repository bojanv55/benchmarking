package me.vukas.benchmarking.benchmarks;

import java.util.ArrayList;
import java.util.List;
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

  public List<Integer> randomList(int min, int max){
    int cap = getInt(min, max);
    List<Integer> res = new ArrayList<>(cap);
    for(int i=0; i<cap; i++){
      res.add(i);
    }
    return res;
  }

}
