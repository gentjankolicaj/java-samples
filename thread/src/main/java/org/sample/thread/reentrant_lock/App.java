package org.sample.thread.reentrant_lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * ReentrantLock is a class that implements interface Lock.ReentrantLock provides mutual exclusion lock with behaviours and semantics as
 * implicit monitor lock accessed using synchronized keyword on methods & statements
 */
@Slf4j
public class App {


  private static final Lock lock = new ReentrantLock();
  private static int counter = 0;

  public static void increment() {
    log.info("Acquired lock.");
    lock.lock();
    try {
      for (int i = 0; i < 10000; i++) {
        counter++;
      }
      //usually we call lock.unlock() on finally block to make sure is executed
    } finally {
      lock.unlock();
      log.info("Released lock.");
    }

  }

  public static void main(String[] args) {
    Thread t1 = new Thread(() -> increment());
    Thread t2 = new Thread(() -> increment());

    t1.start();
    t2.start();
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      log.error("", e);
    }

    log.info("Counter value {}", counter);

  }

}
