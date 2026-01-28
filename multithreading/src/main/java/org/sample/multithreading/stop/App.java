package org.sample.multithreading.stop;

import lombok.extern.slf4j.Slf4j;

/**
 * In this sample I use volatile keyword to read/write variable on RAM, and based on the variable
 * value thread terminates. 1.By using the volatile keyword we guarantee that CPU is not going to
 * cache that variable value. 2.We get the latest value of variable from RAM
 * <p>
 * Note: it might work even without volatile keyword, but we have not guaranteed of latest
 * value=>loop never ends
 * <p>
 * About thread.stop:
 * 1.https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Thread.html#stop()
 * 2.https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/doc-files/threadPrimitiveDeprecation.html
 */
@Slf4j
public class App {

  public static void main(String[] args) throws InterruptedException {
    Worker worker = new Worker();
    Thread t1 = new Thread(worker);

    t1.start();
    Thread.sleep(3000);

    //terminate worker thread from outside.
    worker.setTerminated(true);
    log.info("worker-thread terminated state : {}", worker.isTerminated());

  }

}

@Slf4j
class Worker implements Runnable {

  /**
   * We guarantee CPU is not going to cache this variable
   */
  private volatile boolean terminated;

  @Override
  public void run() {
    log.info("Worker-thread...");
    while (!terminated) {
      log.info("running.");
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    log.info("Worker-thread on termination.");
  }

  public boolean isTerminated() {
    return terminated;
  }

  public void setTerminated(boolean terminated) {
    this.terminated = terminated;
  }
}
