package org.sample.thread.runnable;

public class PrintRunnable implements Runnable {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      System.out.println("DaemonThread: " + i);
    }
  }
}
