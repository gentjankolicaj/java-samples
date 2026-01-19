package org.sample.thread.volatiles;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  private final Lock lock = new ReentrantLock();
  /**
   * Read are performed directly on memory(RAM) not on CPU caches.Also, CPU doesn't cache this variable. 1.Variables can be stored on RAM
   * without volatile keyword 2.Multiple threads may use same cache , if are being run on same CPU core.
   */
  private volatile int counter = 0;

  public static void main(String[] args) {
    App app = new App();
    Thread t1 = new Thread(app::increment);
    Thread t2 = new Thread(app::increment);

    t2.start();
    t1.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {

    }
    log.info("App counter {}", app.counter);
  }

  /**
   * This method uses reentrant lock for more explicit monitor lock operations.I could have used synchronized(this){}
   */
  public void increment() {
    lock.lock();
    try {
      //each operation that is performed on this variable is directly on RAM
      counter++;
    } finally {
      lock.unlock();
    }
  }

}
