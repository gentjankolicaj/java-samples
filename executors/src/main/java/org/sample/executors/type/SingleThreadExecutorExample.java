package org.sample.executors.type;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>SingleThreadExecutor is :
 * <br>1.One of 4 types of executors
 * <br>2.This executor has a single thread, we can execute tasks/operations in sequential manner
 * <br>3.Every process is executed by a new thread.
 */
@Slf4j
public class SingleThreadExecutorExample {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
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
