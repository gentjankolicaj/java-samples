package org.sample.ccollections.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * BlockingQueue is :
 * <br>1.Interface for synchronized queues
 * <br>2.A queue that is thread-safe
 * <br>3.FIFO
 */
@Slf4j
public class BlockingQueueExample {

  public static void main(String[] args) {
    BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
    ExecutorService executor = Executors.newFixedThreadPool(2);

    //create submit threads for execution
    executor.execute(new Consumer(blockingQueue));
    executor.execute(new Producer(blockingQueue));

    //Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
    executor.shutdown();
  }

  @Slf4j
  static class Producer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      int counter = 0;
      try {
        while (true) {
          queue.put(counter);
          log.info("Produced item : {}", counter);
          Thread.sleep(200);
          counter++;
        }
      } catch (InterruptedException e) {
        log.error("Error on thread.", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Slf4j
  static class Consumer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(2000);
        while (true) {
          log.info("Consumed item : {}", queue.take());
          Thread.sleep(300);
        }
      } catch (InterruptedException e) {
        log.error("Error on thread.", e);
        Thread.currentThread().interrupt();
      }
    }
  }

}
