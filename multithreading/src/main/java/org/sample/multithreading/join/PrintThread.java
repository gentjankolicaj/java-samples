package org.sample.multithreading.join;

public class PrintThread extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      System.out.println("DaemonThread: " + i);
    }
  }
}
