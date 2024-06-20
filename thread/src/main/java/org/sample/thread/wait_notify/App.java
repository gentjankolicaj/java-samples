package org.sample.thread.wait_notify;

import lombok.extern.slf4j.Slf4j;

/**
 * Threads that are locking/attempting to lock on same intrinsic lock of an object can :
 * <br> release intrinsic lock and wait : with wait()
 * <br> release intrinsic lock eventually & notify thread that called method wait() : with
 * notify();
 */
@Slf4j
public class App {

  public static void main(String[] args) {
    App app = new App();
    Thread t1 = new Thread(() -> {
      int i = 0;
      do {
        app.produce();
        i++;
      } while (i < 10);
    });

    Thread t2 = new Thread(() -> {
      int i = 0;
      do {
        app.consume();
        i++;
      } while (i < 10);
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
  }

  public void produce() {
    try {
      synchronized (this) {
        log.info("Produce method called.");
        log.info("Calling wait(): Releasing intrinsic lock and waiting for notify...");
        wait();
        log.info("Produce method resumed");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  //start execute this method with thread delay to give time producer
  public void consume() {
    try {
      Thread.sleep(2000);
      synchronized (this) {
        log.info("Consume method called.");
        notify();
        log.info("Called notify(): Eventually releasing intrinsic lock.");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }


}
