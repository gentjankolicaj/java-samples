package org.sample.thread.daemon;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkerThread extends Thread {

  public WorkerThread() {
    super("WorkerThread");
    setDaemon(false); //set this thread as non-daemon=> worker thread
    // => jvm can't finish if this thread hasn't finished execution
  }


  public void run() {
    for (int i = 0; i < 30000; i++) {
      log.info("WorkerThread: {}", i);
    }
    log.info("WorkerThread is terminated.");
  }

}
