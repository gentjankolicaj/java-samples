package org.sample.thread.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * This is an example of Deadlock. Both thread fall into Deadlock and pass into blocked/waiting state.This happens because both threads are
 * trying to acquire a lock owned. * 1.In Deadlock threads are in Waiting/Blocked state , but can't proceed further * 2.In Livelock threads
 * are in active state, but can't proceed further
 */
@Slf4j
public class App {

  private final Lock lock1 = new ReentrantLock(true);
  private final Lock lock2 = new ReentrantLock(true);

  public static void main(String[] args) {
    App app = new App();

    //create threads & run them immediately
    new Thread(app::worker1, "worker1").start();
    new Thread(app::worker2, "worker2").start();
  }

  public void worker1() {
    try {
      lock1.lock();
      log.info("worker1 acquires lock1");
      Thread.sleep(200);

      lock2.lock();
      log.info("worker1 acquires lock2");
    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      lock1.unlock();
      lock2.unlock();
    }

  }

  public void worker2() {
    try {
      lock2.lock();
      log.info("worker2 acquires lock2");
      Thread.sleep(200);
      lock1.lock();
      log.info("worker2 acquires lock1");
    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      lock1.unlock();
      lock2.unlock();
    }
  }
}
