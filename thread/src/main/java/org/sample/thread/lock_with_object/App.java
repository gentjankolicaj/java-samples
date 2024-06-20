package org.sample.thread.lock_with_object;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is an example of consistency that happens when we use synchronized keyword causing to
 * acquire object INTRINSIC LOCK example :
 * <br>public synchronized void print(){}
 * <br> public void print(){
 * <br>synchronized(this/object){
 * <br>do something
 * <br>}
 * <br>}
 */
@Slf4j
public class App {

  private final Object lock1 = new Object();
  private final Object lock2 = new Object();
  public int counter1 = 0;
  public int counter2 = 0;

  public static void main(String[] args) {
    App app = new App();
    Thread t1 = new Thread(() -> {
      int i = 0;
      do {
        app.increment1();
        i++;
      } while (i < 1000);
    });

    Thread t2 = new Thread(() -> {
      int i = 0;
      do {
        app.increment1();
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

    log.info("Counter1 value {}", app.counter1);
    log.info("Counter2 value {}", app.counter2);
  }

  /**
   * We make sure this method is executed only by a single thread at a given time using object level
   * intrinsic lock
   */
  public void increment1() {
    //object level lock
    synchronized (lock1) {
      counter1++;
    }
  }

  /**
   * We make sure this method is executed only by a single thread at a given time using object level
   * intrinsic lock
   */
  public void increment2() {
    //object level lock
    synchronized (lock2) {
      counter2++;
    }
  }


}
