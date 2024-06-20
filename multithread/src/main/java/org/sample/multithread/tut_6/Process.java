package org.sample.multithread.tut_6;

public class Process implements Runnable {

  private int id;

  public Process(int id) {
    this.id = id;
  }

  @Override
  public void run() {
    System.out.println("++>Started :" + toString());

    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("-->Ended :" + toString());

  }

  public String toString() {
    return "Thread-" + id;
  }
}
