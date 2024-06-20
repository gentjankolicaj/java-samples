package org.sample.multithread.tut_9;

import java.util.Scanner;

public class Process {

  public void produce() throws InterruptedException {
    synchronized (this) { // I use the intrinsic lock of Process object

      System.out.println("Started thread 1.");

      wait(); //Makes thread wait and lose control of intrinsic lock

      System.out.println("Resumed at thread 1");
      System.out.println("Thread 1 finished exec.");

    }

  }

  public void consume() throws InterruptedException {
    System.out.println("Started thread 2");
    Scanner scanner = new Scanner(System.in);
    Thread.sleep(2000);

    synchronized (this) {

      System.out.println("Waiting for return key.");
      scanner.nextLine();
      notify();
      System.out.println("Return key pressed.");
      System.out.println("Thread 2 finished exec.");
    }
  }
}
