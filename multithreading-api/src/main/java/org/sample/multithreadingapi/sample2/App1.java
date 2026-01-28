package org.sample.multithreadingapi.sample2;

import java.util.Scanner;

class Processor extends Thread {

  //volatile used to prevent threads from caching variables when they are not changed by that thread
  private volatile boolean running = true;

  public void run() {
    System.out.println("Inside infinite loop.");
    while (running) {

      try {

        Thread.sleep(2000);
        System.out.println("infinite loop.");

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

      }
    }
  }

  public void shutdown() {
    running = false;
  }

}

public class App1 {

  public static void main(String[] args) {
    Processor proc = new Processor();
    proc.start();
    proc.start();//I create the thread now,and thread starts running

    Scanner scanner = new Scanner(System.in);
    System.out.println("Press to shutdown...");
    scanner.nextLine();
    proc.shutdown();

  }

}
