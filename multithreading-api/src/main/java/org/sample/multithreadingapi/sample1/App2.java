package org.sample.multithreadingapi.sample1;

public class App2 {

  public static void main(String[] args) {
    CustomRunnable runnable = new CustomRunnable();

    for (int i = 0; i < 100; i++) {
      Thread threadObj = new Thread(runnable, "custom-thread-" + i);
      System.out.println(threadObj.getName());
      threadObj.start();
    }
  }

}
