package org.sample.thread.priority;

import lombok.extern.slf4j.Slf4j;
import org.sample.thread.exception.JoinException;

@Slf4j
public class App {

  public static void main(String[] args) {
    Thread mainThread = Thread.currentThread();
    log.info("thread-main name , {}", mainThread.getName());
    log.info("thread-main priority , {}", mainThread.getPriority());

    //set thread-main priority to max
    mainThread.setPriority(Thread.MAX_PRIORITY);
    log.info("thread-main priority , {}", mainThread.getPriority());

    //We have achieved multithreading because of multi-threads, not parallelization.
    Thread t1 = new DaemonThread();
    Thread t2 = new NonDaemonThread();

    //after start() is called thread is in active state
    t1.start();
    t2.start();

    //we can wait for thread to finish with help of join method.
    try {
      t1.join();
      // t2.join(); since t2 is non-daemon thread, JVM can't terminate without it finishing execution => I don't have to wait on main thread.
    } catch (InterruptedException e) {
      throw new JoinException(e);
    }
    log.info("main-thread finished.");

  }

}
