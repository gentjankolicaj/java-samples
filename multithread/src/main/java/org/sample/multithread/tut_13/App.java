package org.sample.multithread.tut_13;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class App {

  public static void main(String[] args) {
    Semaphore sem = new Semaphore(5); //5 available permits

    Connection conn = Connection.getInstance();

    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 0; i < 43; i++) {
      executor.submit(new Runnable() {
        public void run() {

          conn.connect();


        }
      });
    }

    System.out.println("Available permits:" + sem.availablePermits());

    executor.shutdown();

    try {

      executor.awaitTermination(1, TimeUnit.DAYS);

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

}
