package org.sample.thread.join;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogThread extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      log.info("NonDaemonThread: {}", i);
    }
  }
}
