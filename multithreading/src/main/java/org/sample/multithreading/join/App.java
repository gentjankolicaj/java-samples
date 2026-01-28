package org.sample.multithreading.join;

import lombok.extern.slf4j.Slf4j;
import org.sample.multithreading.exception.JoinException;

@Slf4j
public class App {

  public static void main(String[] args) {

    //We have achieved multithreading because of multi-threads, not parallelization.
    Thread t1 = new LogThread();
    Thread t2 = new PrintThread();

    //after start() is called thread is in active state
    t1.start();
    t2.start();

    //we can wait for thread to finish with help of join method.
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      throw new JoinException(e);
    }
    log.info("Threads t1,t2 finished executing.");

  }

}

