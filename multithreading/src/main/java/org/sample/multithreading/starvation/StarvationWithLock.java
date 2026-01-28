package org.sample.multithreading.starvation;


import lombok.extern.slf4j.Slf4j;

/**
 * <p>This is an example of starvation in multithreading.</p>
 * <p>Starvation happens when a thread never gets CPU time or access to a resource because other
 * threads keep taking it (often due to priority or unfair locking).</p>
 */
@Slf4j
public class StarvationWithLock {

  public static void main(String[] args) {
    Resource resource = new Resource();

    // Greedy threads
    Runnable greedyTask = () -> {
      long count = 0;
      while (count < Integer.MAX_VALUE) {
        count++;
        resource.use();
      }
    };

    // Starving thread
    Runnable starvingTask = () -> {
      long count = 0;
      while (count < Integer.MAX_VALUE) {
        count++;
        resource.use();
      }
    };

    new Thread(greedyTask, "Greedy-1").start();
    new Thread(greedyTask, "Greedy-2").start();
    new Thread(starvingTask, "Starving-Thread").start();
  }

  static class Resource {

    synchronized void use() {
      log.info("{} acquired lock", Thread.currentThread().getName());
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        //do nothing
      }
    }
  }
}
