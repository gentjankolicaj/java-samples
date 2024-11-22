package org.sample.multithread.tut_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {

  private final int id;

  public Processor(int id) {
    this.id = id;
  }

  @Override
  public void run() {
    System.out.println();
    System.out.println("Starting: " + id);

    try {

      Thread.sleep(5000);

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

    }

    System.out.println("Completed: " + id);

  }

}

public class App1 {

  public static void main(String[] args) {

    ExecutorService executorService = Executors.newFixedThreadPool(
        2); //I define a max of threads to exist in same time

    for (int i = 0; i < 5; i++) {
      executorService.submit(new Processor(i));
    }

    executorService.shutdown();

    System.out.println("All task submited.");

    try {
      executorService.awaitTermination(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
