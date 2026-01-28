package org.sample.multithreading.wait_notify;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Threads that are locking/attempting to lock on same intrinsic lock of an object can :
 * <br> release intrinsic lock and wait : with wait()
 * <br> release intrinsic lock eventually & notify thread that called method wait() : with
 * notify();
 */
@Slf4j
public class Processor {

  private static final int UPPER_LIMIT = 10;
  private static final int LOWER_LIMIT = 0;
  private final List<Integer> list = new ArrayList<>();

  public static void main(String[] args) {
    Processor app = new Processor();
    Thread t1 = new Thread(() -> {
      app.produce();
    });

    Thread t2 = new Thread(() -> {
      app.consume();
    });

    //by calling method start(), threads change state from new=>active.
    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      log.error("", e);
    }
  }

  public void produce() {
    int start = 0;
    try {
      while (true) {
        synchronized (this) {
          if (list.size() == UPPER_LIMIT) {
            log.info("Producer filled list with {}", list);
            wait();
          } else {
            list.add(start);
            start++;
            notify();
          }
          Thread.sleep(100);
        }
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  //start execute this method with thread delay to give time producer
  public void consume() {
    try {
      Thread.sleep(2000);
      while (true) {
        synchronized (this) {
          if (list.size() == LOWER_LIMIT) {
            log.info("Consumer consume all values, waiting producer thread");
            wait();
          } else {
            Integer value = list.remove(list.size() - 1);
            log.info("Consumed : {} ", value);
            notify();
          }
          Thread.sleep(100);
        }
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }


}
