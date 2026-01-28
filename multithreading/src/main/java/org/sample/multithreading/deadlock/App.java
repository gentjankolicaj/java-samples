package org.sample.multithreading.deadlock;

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
    new Thread(app::task1, "worker1").start();
    new Thread(app::task2, "worker2").start();
  }

  public void task1() {
    try {
      lock1.lock();
      log.info("worker1 acquires lock1");
      Thread.sleep(200);

      lock2.lock(); //note: deadlock is archived because lock2 is not release by task2
      log.info("worker1 acquires lock2");
    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      lock1.unlock();
      lock2.unlock();
    }

  }

  public void task2() {
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
