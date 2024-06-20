package org.sample.ccollections.map;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrentHashMapExample {

  public static void main(String[] args) {
    ConcurrentMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    //Submit new threads for execution to executor
    executor.execute(new Consumer(concurrentMap));
    executor.execute(new Producer(concurrentMap));

    //Initiate orderly shutdown not forced
    executor.shutdown();
  }

  @RequiredArgsConstructor
  @Slf4j
  static class Producer implements Runnable {

    private final ConcurrentMap<Integer, String> map;

    @Override
    public void run() {
      try {
        int key = 0;
        while (true) {
          map.put(key, "Value_" + key);
          log.info("Produced on map {}", map.get(key));
          key++;
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

    private final ConcurrentMap<Integer, String> map;

    @Override
    public void run() {
      try {
        int key = 0;
        while (true) {
          String value = map.get(key);
          if (Objects.nonNull(value)) {
            map.remove(key);
            log.info("Consumed on map : key: {}, value: {}", key, value);
            key++;
            Thread.sleep(200);
          } else {
            //I sleep more millis & don't increment key value in order to iterate again & try to get value for key
            Thread.sleep(500);
          }
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error : ", e);
        Thread.currentThread().interrupt();
      }
    }
  }

}
