package org.sample.multithreading.livelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>This is an example of Deadlock.</p>
 * <p>Both thread fall into Deadlock and pass into blocked/waiting state.This happens because both
 * threads are trying to acquire a lock owned.</p>
 * <p>1.In Deadlock threads are in Waiting/Blocked state , but can't proceed further </p>
 * <p>2.In Livelock threads are in active state, but can't proceed further</p>
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
    while (true) {
      try {
        lock1.tryLock(50, TimeUnit.MILLISECONDS);
        log.info("worker1 acquires lock1");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (lock2.tryLock()) {
        log.info("worker1 acquires lock2");
        lock2.unlock();
      } else {
        log.info("worker1 can't acquire lock2");
        continue;
      }
      break;
    }
    lock1.unlock();
    lock2.unlock();
  }

  public void worker2() {
    while (true) {
      try {
        lock2.tryLock(50, TimeUnit.MILLISECONDS);
        log.info("worker2 acquires lock1");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (lock1.tryLock()) {
        log.info("worker2 acquires lock1");
        lock1.unlock();
      } else {
        log.info("worker2 can't acquire lock1");
        continue;
      }
      break;
    }
    lock1.unlock();
    lock2.unlock();
  }
}
