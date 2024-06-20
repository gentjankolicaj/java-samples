package org.sample.multithread.tut_9;

public class App {

  public static void main(String[] args) {

    final Process proc = new Process();

    // I create thread objects,using annonymous class implementation
    Thread t1 = new Thread(new Runnable() {

      public void run() {
        try {

          proc.produce();

        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {

      public void run() {
        try {

          proc.consume();

        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    // I start threads
    t1.start();
    t2.start();

    // I wait for thread to finish execution

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }

  }

}
