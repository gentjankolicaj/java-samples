package org.sample.thread.thread_class;

public class App {

  public static void main(String[] args) {

    //We have achieved multithreading because of multi-threads not parallelization.
    Thread t1 = new LogRunnable();
    Thread t2 = new PrintRunnable();

    //after start() is called thread is in active state
    t1.start();
    t2.start();
  }

}
