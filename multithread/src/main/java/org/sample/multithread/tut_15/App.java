package org.sample.multithread.tut_15;

import java.util.Random;

public class App {

  public static void main(String[] args) {

    Thread t1 = new Thread(new Runnable() {

      public void run() {
        System.out.println("Running thread t1");
        Random rand = new Random();

        for (int i = 0; i < 1E6; i++) {
          System.out.println(Math.sin(i));
        }

        Thread currentThread = Thread.currentThread();

        if (currentThread.isInterrupted()) {
          System.out.println("Interrupt flag is set for this thread");
        }

      }
    });

    t1.start();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    t1.interrupt(); //set flag so thread can be interrupted automatically

    try {

      t1.join();

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("Finished thread");

  }

}
