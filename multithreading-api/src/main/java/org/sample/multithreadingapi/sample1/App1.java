package org.sample.multithreadingapi.sample1;

//we don't need import because thread is part of java.lang package.("this package is used automatically")
class MyRunner extends Thread {

  @Override
  public void run() {

    for (int i = 0; i < 50; i++) {
      System.out.println("-> " + i);
    }

    try {
      System.out.println("Thread about to stop for 5s.");
      Thread.sleep(5000); // stops the executing thread to pause for 5 seconds

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("Thread started again");
  }

}

public class App1 {

  public static void main(String[] args) {

    MyRunner runner = new MyRunner();
    MyRunner runner2 = new MyRunner();

    runner.start();
    runner2.start();
  }

}
