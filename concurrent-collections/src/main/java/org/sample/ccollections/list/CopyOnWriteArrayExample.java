package org.sample.ccollections.list;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CopyOnWriteArrayExample {

  public static void main(String[] args) {
    List<Integer> list = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);
    //Note:CopyOnWriteArrayList is more efficient than Vector even thought vector is Thread-safe
    List<Integer> concurrentList = new CopyOnWriteArrayList<>(list);
    ExecutorService executor = Executors.newFixedThreadPool(3);

    //Create multiple producer threads that update list
    executor.execute(new Producer(concurrentList));
    executor.execute(new Producer(concurrentList));

    //Create consumer thread
    executor.execute(new Consumer(concurrentList));

    //Initiate orderly shutdown not forced
    executor.shutdown();
  }

  @RequiredArgsConstructor
  @Slf4j
  static class Producer implements Runnable {

    private final Random random = new Random();

    private final List<Integer> list;

    @Override
    public void run() {
      try {
        while (true) {
          int index = random.nextInt(list.size());
          int value = random.nextInt(10);
          list.set(index, value);
          log.info("Produced list {}", list);
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

    private final List<Integer> list;

    @Override
    public void run() {
      try {
        while (true) {
          log.info("Consumed list : {}", list);
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error : ", e);
        Thread.currentThread().interrupt();
      }
    }
  }


}
