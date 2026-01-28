package org.sample.multithreading.runnable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogRunnable implements Runnable {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      log.info("NonDaemonThread: {}", i);
    }
  }
}
