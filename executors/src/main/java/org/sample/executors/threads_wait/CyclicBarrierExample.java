package org.sample.executors.threads_wait;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * CyclicBarrier is :
 * <br>1.A class for synchronization aid that allows a set of threads to all wait for each other to
 * reach a common barrier point.
 * <br>2.CyclicBarriers are useful in programs involving a fixed sized party of threads that must
 * occasionally wait for each other
 * <br>3.Is cyclic because it can be reused after waiting threads are released.
 * <br>4.await() method pauses threads to wait till all threads have reached this point
 * <br>5.CB also allows for an optional Runnable once per barrier point,after last thread in the
 * party arrives but before any threads are released.
 * <br>6.It can be reused.
 */
@Slf4j
public class CyclicBarrierExample {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newCachedThreadPool();

    //Set CyclicBarrier to 10
    //All 10 thread need to arrive at  cyclicBarrier.await()
    CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> log.info("CyclicBarrier reached."));
    for (int i = 0; i < 10; i++) {
      executor.execute(new Task(i, cyclicBarrier));
    }

    //initiate orderly shutdown
    executor.shutdown();
  }

  @Slf4j
  static class Task implements Runnable {

    int id;
    CyclicBarrier cyclicBarrier;

    Task(int id, CyclicBarrier cyclicBarrier) {
      this.id = id;
      this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
      log.info("running task on thread {} | id {}", Thread.currentThread(), id);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        log.error("Interrupting thread because of error : ", e);
        Thread.currentThread().interrupt();
      }

      try {
        //stop all threads with this barrier point
        //when all threads call await() barrier is broken
        cyclicBarrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        log.error("Error ", e);
        log.warn("Error at CyclicBarrier point,interrupting thread...");
        Thread.currentThread().interrupt();
      }
      log.info("After await , thread {}", Thread.currentThread());
    }
  }


}
