package org.sample.multithread.tut_7;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

  public static void main(String[] args) {

    final CountDownLatch latch = new CountDownLatch(5);

    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 0; i < 33; i++) {

      executor.submit(new Process(latch, i));
    }

    try {

      latch.await(); //wait till countdown latch is ==0

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("Completed.");
  }
}
