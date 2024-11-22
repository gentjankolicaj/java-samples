package org.sample.multithread.tut_6;

public class Process implements Runnable {

  private final int id;

  public Process(int id) {
    this.id = id;
  }

  @Override
  public void run() {
    System.out.println("++>Started :" + this);

    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("-->Ended :" + this);

  }

  public String toString() {
    return "Thread-" + id;
  }
}
