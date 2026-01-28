package org.sample.multithreading.runnable;

public class App {

  public static void main(String[] args) {
    Runnable log = new LogRunnable();
    Runnable print = new PrintRunnable();

    //We have achieved multithreading because of multi-threads not parallelization.
    Thread t1 = new Thread(log);
    Thread t2 = new Thread(print);

    t1.start();
    t2.start();
  }

}
