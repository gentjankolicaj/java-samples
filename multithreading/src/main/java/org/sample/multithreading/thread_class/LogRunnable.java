package org.sample.multithreading.thread_class;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogRunnable extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      log.info("NonDaemonThread: {}", i);
    }
  }
}
