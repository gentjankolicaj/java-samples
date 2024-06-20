package org.sample.future;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureSample {

  static final ExecutorService executorService = Executors.newWorkStealingPool();

  public static void main(String[] args) {
    int a = 0, b = 11;
    Callable<Integer> add = () -> a + b;
    Callable<Integer> subtract = () -> a - b;

    Future<Integer> futureAdd = executorService.submit(add);
    Future<Integer> futureSubtract = executorService.submit(subtract);

    delayInSeconds(10);
  }

  static void delayInSeconds(int delay) {
    try {
      TimeUnit.SECONDS.sleep(delay);
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

}
