package org.sample.multithreading.starvation;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>This is an example of starvation in multithreading.</p>
 * <p>Starvation happens when a thread never gets CPU time or access to a resource because other
 * threads keep taking it (often due to priority or unfair locking).</p>
 */
@Slf4j
public class StarvationWithLowPriority {

  public static void main(String[] args) throws InterruptedException {
    HighPriorityThread highPriorityThread = new HighPriorityThread();
    LowPriorityThread lowPriorityThread = new LowPriorityThread();

    //start both threads.
    highPriorityThread.start();
    lowPriorityThread.start();

    lowPriorityThread.join();
    highPriorityThread.join();
  }

  static class LowPriorityThread extends Thread {

    public LowPriorityThread() {
      super("Low-Priority-Thread");
      this.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
      log.info("Low priority thread started...");
      long count = 0;
      while (count < Integer.MAX_VALUE) {
        count++;
        log.info("Low priority thread ran {} times ", count);
      }
    }
  }

  static class HighPriorityThread extends Thread {

    public HighPriorityThread() {
      super("High-Priority-Thread");
      this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
      log.info("High priority thread started...");
      long count = 0;
      while (count < Integer.MAX_VALUE) {
        count++;
        log.info("High priority thread ran {} times...", count);
      }
    }
  }
}
