package org.sample.multithreadingapi.sample1;

public class App3 {

  public static void main(String[] args) {

    //anonymous class implementation of runnable method
    Thread t1 = new Thread(new Runnable() {

      public void run() {

        for (int i = 0; i < 100; i++) {
          System.out.println(i);
        }

        try {
          Thread.sleep(8000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        System.out.println("Thread started again");
      }
    });

    t1.start();

    //Start thread now
    StackTraceElement[] array = t1.getStackTrace();
    for (StackTraceElement el : array) {
      System.out.println(el.getMethodName());
    }

  }

}
