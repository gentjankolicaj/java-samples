package org.sample.thread.daemon;

/**
 * Daemon threads are intended as helper threads
 */
public class DaemonThread extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 200; i++) {
      System.out.println("DaemonThread: " + i);
    }
  }
}
