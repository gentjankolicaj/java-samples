package org.sample.vthread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreads {

  /**
   * Note: platform & virtual threads are not the only ones running when jvm started. Jvm also has other threads like : 1.GC threads 2.JIT
   * threads 3.Main thread
   */
  public static void main(String[] args) throws InterruptedException {
    runPlatformThreads();
    runVirtualThreads();
  }

  private static void runVirtualThreads() throws InterruptedException {
    var vThread = Thread.ofVirtual().unstarted(() -> log.info("{}", Thread.currentThread()));
    vThread.start();
    vThread.join();
    log.info("Class : " + vThread.getClass());
  }

  private static void runPlatformThreads() throws InterruptedException {
    var pThread = Thread.ofPlatform()
        .unstarted(() -> log.info("{}", Thread.currentThread()));

    pThread.start();
    pThread.join();
  }
}
