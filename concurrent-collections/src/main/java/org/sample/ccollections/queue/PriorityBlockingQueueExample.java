package org.sample.ccollections.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * PriorityBlockingQueue is :
 * <br>1.Class that implements BlockingQueue
 * <br>2.Unbounded concurrent queue => Thread-safe FIFO
 * <br>3.Uses ordering rules
 * <br>4.No null items
 * <br>5.Priority decide by compareTo() method.
 */
@Slf4j
public class PriorityBlockingQueueExample {

  public static void main(String[] args) {
    BlockingQueue<Element> blockingQueue = new PriorityBlockingQueue<>();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    //Submit new threads for execution to executor
    executor.execute(new Consumer(blockingQueue));
    executor.execute(new Producer(blockingQueue));

    //Initiate orderly shutdown
    executor.shutdown();

  }

  @RequiredArgsConstructor
  @Getter
  @ToString
  static class Element implements Comparable<Element> {

    /**
     * Index used for ordering on queue
     */
    private final int index;


    @Override
    public int compareTo(Element element) {
      //PriorityQueue decides order based on value of compareTo
      return getIndex() - element.getIndex();
    }
  }

  @Slf4j
  @RequiredArgsConstructor
  static class Producer implements Runnable {

    private final BlockingQueue<Element> queue;

    @Override
    public void run() {
      try {
        Random random = new Random(100);
        Thread.sleep(100);
        while (true) {
          Element element = new Element(random.nextInt(100));
          queue.put(element);
          log.info("Produced element : {}", element);
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error :", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Slf4j
  @RequiredArgsConstructor
  static class Consumer implements Runnable {

    private final BlockingQueue<Element> queue;

    @Override
    public void run() {
      try {
        Thread.sleep(1000);
        while (true) {
          log.info("Consumed element : {}", queue.take());
          Thread.sleep(200);
        }
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error :", e);
        Thread.currentThread().interrupt();
      }
    }
  }

}
