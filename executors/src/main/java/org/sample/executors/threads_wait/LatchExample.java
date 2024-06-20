package org.sample.executors.threads_wait;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * CountDownLatch is :
 * <br>1.Class for synchronization aid that allows one or more threads to wait until a set of
 * operations being
 * performed in other threads completes.
 * <br>2.CDL is initialized with a given count and counter can't be reset.
 * <br>3.wait() method blocks waiting threads until current count reaches 0 due to invocations of
 * countDown()
 * <br>4.countDown() decreases counter of CDL
 * <a
 * href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/CountDownLatch.html">link</a>
 */
@Slf4j
public class LatchExample {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(4);

    //Set countdown-latch to 10
    //It needs to be decreased 10 times in order for main thread to continue past latch.await()
    CountDownLatch latch = new CountDownLatch(10);
    for (int i = 0; i < 10; i++) {
      executor.execute(new Task(i, latch));
    }

    //block main-thread & wait until countdown-latch==0
    try {
      latch.await();
    } catch (InterruptedException e) {
      log.error("Error ", e);
      log.warn("Interrupting thread...");
      Thread.currentThread().interrupt();
    }
    log.info("CountDownLatch is 0.");

    //initiate orderly shutdown
    executor.shutdown();

  }

  @Slf4j
  static class Task implements Runnable {

    int id;
    CountDownLatch latch;

    Task(int id, CountDownLatch latch) {
      this.id = id;
      this.latch = latch;
    }


    @Override
    public void run() {
      log.info("running task on thread {} | id {}", Thread.currentThread(), id);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      //decrease latch
      latch.countDown();
    }
  }
}
