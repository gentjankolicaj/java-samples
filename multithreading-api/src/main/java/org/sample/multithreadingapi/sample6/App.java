package org.sample.multithreadingapi.sample6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

  public static void main(String[] args) {
    //I define a thread pool with max size of 5
    int maxSize = 5;

    ExecutorService executor = Executors.newFixedThreadPool(
        maxSize); //5 threads are going to run simultaneously

    for (int i = 0; i < 75; i++) {
      Process proc = new Process(i);

      executor.submit(proc);

    }

    executor.shutdown();
    System.out.println("All threads run.");

    try {

      executor.awaitTermination(1, TimeUnit.DAYS); //wait one day if thread are not finished
      //if threads are finished shuts down.

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
