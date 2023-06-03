package org.sample.ccollections.exchanger;


import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This is an example of exchanger pattern when 2 threads own each 1 object & they exchange them.
 */
@Slf4j
public class ExchangerExample {

  public static void main(String[] args) {
    Exchanger<Integer> exchanger = new Exchanger<>();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    //Submit new threads for execution to executor
    executor.execute(new Consumer(exchanger));
    executor.execute(new Producer(exchanger));

    //Initiate orderly shutdown not forced
    executor.shutdown();
  }

  @RequiredArgsConstructor
  @Slf4j
  static class Producer implements Runnable {

    private final Exchanger<Integer> exchanger;
    private int counter;

    @Override
    public void run() {
      try {
        while (true) {
          counter++;
          int consumerCounter = exchanger.exchange(counter);
          log.info("Producer : counter {} | consumerCounter {}", counter, consumerCounter);
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error : ", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @RequiredArgsConstructor
  @Slf4j
  static class Consumer implements Runnable {

    private final Exchanger<Integer> exchanger;
    private int counter = 100;

    @Override
    public void run() {
      try {
        while (true) {
          counter++;
          int producerCounter = exchanger.exchange(counter);
          log.info("Consumer : counter {} | producerCounter {}", counter, producerCounter);
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error : ", e);
        Thread.currentThread().interrupt();
      }
    }
  }

}
