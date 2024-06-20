package org.sample.thread.class_level_sync;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is an example of consistency that happens when we use synchronized keyword causing to
 * acquire class INTRINSIC LOCK
 */
@Slf4j
public class App {

  public static int counter = 0;

  /**
   * We make sure this method is executed only by a single thread at a given time using class level
   * intrinsic lock
   */
  public static synchronized void increment1() {
    counter++;
  }

  /**
   * We make sure this method is executed only by a single thread at a given time using class level
   * intrinsic lock
   */
  public static void increment2() {
    //class level lock
    synchronized (App.class) {
      counter++;
    }
  }


  public static void main(String[] args) {
    Thread t1 = new Thread(() -> {
      int i = 0;
      do {
        increment1();
        i++;
      } while (i < 1000);
    });

    Thread t2 = new Thread(() -> {
      int i = 0;
      do {
        increment1();
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
