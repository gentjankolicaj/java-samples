package org.sample.multithread.tut_1;

public class CustomRunnable implements Runnable {

  @Override
  public void run() {
    System.out.println(CustomRunnable.class.getName());
    for (long i = 0; i < 1000; i++) {
      System.out.println("+> " + i);
    }

    try {
      System.out.println(CustomRunnable.class.getName() + ": thread about to stop.");
      Thread.sleep(5000);
      System.out.println(CustomRunnable.class.getName() + ": thread started again.");
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
