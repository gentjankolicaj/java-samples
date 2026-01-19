package org.sample.thread.inconsistency;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is an example of inconsistency that happens when increment of a variable is not atomic Note : operation (++) itself is atomic
 * but method is called by multiple threads.
 */
@Slf4j
public class App {

  public static int counter = 0;

  public static void increment() {
    counter++;
  }


  public static void main(String[] args) {
    Thread t1 = new Thread(() -> {
      int i = 0;
      do {
        increment();
        i++;
      } while (i < 1000);
    });

    Thread t2 = new Thread(() -> {
      int i = 0;
      do {
        increment();
        i++;
      } while (i < 1500);
    });

    //by calling method start(), threads change state from new=>active.
    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      log.error("", e);
    }

    log.info("Counter value {}", counter);
  }


}
