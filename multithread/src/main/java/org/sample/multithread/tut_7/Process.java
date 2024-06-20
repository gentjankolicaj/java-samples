package org.sample.multithread.tut_7;

import java.util.concurrent.CountDownLatch;

public class Process implements Runnable {

  private CountDownLatch latch;
  private int id;


  public Process(CountDownLatch latch, int id) {
    super();
    this.latch = latch;
    this.id = id;
  }


  @Override
  public void run() {
    System.out.println("++> Started " + toString());

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    latch.countDown();
  }

  public String toString() {
    return "Thread-" + id;
  }

}
