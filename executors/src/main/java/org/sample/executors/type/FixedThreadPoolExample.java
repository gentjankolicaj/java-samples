package org.sample.executors.type;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>FixedThreadPool(n) is :
 * <br>1.One of 4 types of executors
 * <br>2.This executor has n number of threads, we can execute n tasks/operations in concurrent
 * manner
 * <br>3.This is how we create a thread pool with n threads
 * <br>4.If there are more task than number of threads,tasks are stored in LinkedBlockingQueue for
 * later execution.
 */
@Slf4j
public class FixedThreadPoolExample {

  public static void main(String[] args) {
    int processorsAvailable = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(processorsAvailable);
    for (int i = 0; i < 20; i++) {
      executor.execute(new Task(i));
    }

    //prevent new tasks & initiate orderly shutdown
    executor.shutdown();

    //terminate actual/running tasks
    try {
      if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
        //immediately to stop all active tasks
        // executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      log.error("", e);
      executor.shutdownNow();
    }

  }

  static class Task implements Runnable {

    int id;

    Task(int id) {
      this.id = id;
    }

    @Override
    public void run() {
      log.info("Executing task {} on thread : {}", id, Thread.currentThread().getId());
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
    }
  }
}
