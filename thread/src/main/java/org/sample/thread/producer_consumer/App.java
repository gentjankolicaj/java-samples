package org.sample.thread.producer_consumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * ReentrantLock is a class that implements interface Lock.ReentrantLock provides mutual exclusion lock with behaviours and semantics as
 * implicit monitor lock accessed using synchronized keyword on methods & statements
 */
@Slf4j
public class App {


  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  public static void main(String[] args) {
    App app = new App();
    Thread t1 = new Thread(app::produce);
    Thread t2 = new Thread(app::consume);

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
    lock.lock();
    try {
      log.info("Produce method exec...");
      //make thread stop execution & release intrinsic lock
      condition.await();
      log.info("Produce method restarted exec...");

    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      //release intrinsic lock
      lock.unlock();
    }
  }

  public void consume() {
    try {
      //sleep to give time produce to acquire lock first
      Thread.sleep(2000);
      lock.lock();
      log.info("Consumer method exec...");

      //wake up waiting thread
      condition.signal();
      log.info("Consumer method signaled.");
      log.info("Consumer method restarted exec...");

    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      //release intrinsic lock
      lock.unlock();
    }
  }

}
