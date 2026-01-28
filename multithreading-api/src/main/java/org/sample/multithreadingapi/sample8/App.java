package org.sample.multithreadingapi.sample8;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {

  private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue(13);

  public static void main(String[] args) {
    // I create thread objects

    Thread t1 = new Thread(new Runnable() {

      public void run() {
        producer();
      }
    });

    Thread t2 = new Thread(new Runnable() {

      public void run() {
        consumer();
      }
    });

    // Start threads
    t1.start();
    t2.start();

    // wait for started threads to finish executing so I can execute SOP line

    try {
      t1.join();
      t2.join();

    } catch (InterruptedException e) {

    }

    System.out.println("Thread finished executing.");
  }

  private static void producer() {

    Random random = new Random();
    while (true) { // inifinite loop so thread dosent terminate

      try {

        queue.put(random.nextInt(100));

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

      }
    }
  }

  private static void consumer() {
    Random random = new Random();

    while (true) { // inifinite loop so thread dosent terminate
      try {

        Thread.sleep(500);
        if (random.nextInt(10) == 0) {
          Integer value = queue.take();
          System.out.println("Taken value:" + value + ",queue size:" + queue.size());
        }

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
