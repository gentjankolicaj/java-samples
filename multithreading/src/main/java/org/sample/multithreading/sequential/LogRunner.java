package org.sample.multithreading.sequential;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LogRunner implements Runner {

  public void execute() {
    for (int i = 0; i < 100; i++) {
      log.info("LogRunner : {}", i);
    }
  }
}
