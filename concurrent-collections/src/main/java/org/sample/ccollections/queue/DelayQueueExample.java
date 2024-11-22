package org.sample.ccollections.queue;


import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


/**
 * DelayQueue is :
 * <br>1.Class implementation of BlockingQueue
 * <br>2.FIFO
 * <br>3.Queue thread-safe
 * <br>4.Unbounded queue
 * <br>5.Elements have expiry delay
 */
public class DelayQueueExample {

  public static void main(String[] args) {
    //delay queue is of type blockingQueue
    DelayQueue<Element> delayQueue = new DelayQueue<>();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    //create submit threads for execution
    executor.execute(new Consumer(delayQueue));
    executor.execute(new Producer(delayQueue));

    //Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
    executor.shutdown();

  }

  @Slf4j
  static class Producer implements Runnable {

    private final BlockingQueue<Element> queue;

    public Producer(BlockingQueue<Element> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      //we start duration from 1000=> 1 second
      long duration = 1000;
      try {
        while (true) {
          queue.put(new Element(duration, "Attempt_" + duration));
          log.info("Produced item : {}", duration);
          Thread.sleep(1000);
          duration = duration + 500;
        }
      } catch (InterruptedException e) {
        log.error("Error on thread.", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Slf4j
  static class Consumer implements Runnable {

    private final BlockingQueue<Element> queue;

    public Consumer(BlockingQueue<Element> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(2000);
        while (true) {
          log.info("Consumed item : {}", queue.take());
          Thread.sleep(3000);
        }
      } catch (InterruptedException e) {
        log.error("Error on thread.", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Getter
  @Setter
  @ToString
  static class Element implements Delayed {

    private long duration;
    private String value;

    public Element(long duration, String value) {
      this.duration = System.currentTimeMillis() + duration;
      this.value = value;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
      if (Objects.isNull(timeUnit)) {
        return 0;
      } else {
        //duration=constant+currentTime
        //delay=duration-currentTime
        return timeUnit.convert(duration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
      }
    }

    @Override
    public int compareTo(Delayed delayed) {
      if (duration < ((Element) delayed).getDuration()) {
        return -1;
      }
      if (duration == ((Element) delayed).getDuration()) {
        return 0;
      }
      return +1;
    }

  }

}
