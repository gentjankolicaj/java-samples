package org.sample.executors.type;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>ScheduledExecutor is :
 * <br>1.One of 4 types of executors
 * <br>2.We can execute an operation at regular intervals
 * <br>3.We can use this executor if we wish to delay tasks.
 */
@Slf4j
public class ScheduledExecutorExample {

  public static void main(String[] args) {
    int processorsAvailable = Runtime.getRuntime().availableProcessors();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(processorsAvailable);

    //calls StockTask every 2000 millis
    executor.scheduleAtFixedRate(new StockTask(1), 500L, 2000L, TimeUnit.MILLISECONDS);

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

  static class StockTask implements Runnable {

    int id;

    StockTask(int id) {
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
