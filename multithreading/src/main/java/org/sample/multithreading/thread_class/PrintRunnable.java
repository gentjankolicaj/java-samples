package org.sample.multithreading.thread_class;

public class PrintRunnable extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      System.out.println("DaemonThread: " + i);
    }
  }
}
