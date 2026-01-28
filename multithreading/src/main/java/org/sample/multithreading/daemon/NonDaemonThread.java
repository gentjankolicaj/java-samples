package org.sample.multithreading.daemon;

import lombok.extern.slf4j.Slf4j;

/**
 * Non-daemon threads are worker threads.
 */
@Slf4j
public class NonDaemonThread extends Thread {

  public NonDaemonThread() {
    super("NonDaemonThread");
    setDaemon(
        false); //set this thread to non-daemon=> worker thread=>JVM can't terminate without non-daemon finish execution
  }

  @Override
  public void run() {
    for (int i = 0; i < 20000; i++) {
      log.info("NonDaemonThread: {}", i);
    }
    log.info("NonDaemonThread is terminated.");
  }


}
