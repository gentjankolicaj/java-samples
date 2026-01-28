package org.sample.multithreadingapi.sample12;


public class App {

  public static void main(String[] args) {
    final Runner runner = new Runner();

    // I create thread objects,using annonymous class implementation
    Thread t1 = new Thread(new Runnable() {

      public void run() {
        try {

          runner.firstThread();

        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {

      public void run() {
        try {

          runner.secondThread();

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

    runner.finished();

  }

}
