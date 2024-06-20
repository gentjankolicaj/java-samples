package org.sample.multithread.tut_11;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

  private int count = 0;
  private Lock lock = new ReentrantLock();
  private Condition cond = lock.newCondition();

  public void increment() {
    for (int i = 0; i < 10000; i++) {
      count++;
    }
  }

  public void firstThread() throws InterruptedException {
    lock.lock();

    cond.await(); //==wait() ,stops thread hands over the lock

    System.out.println("Other thread awakened.");
    try {
      increment();
    } catch (Exception e) {

      e.printStackTrace();

    } finally {

      lock.unlock(); //in case exception is thrown

    }
  }

  public void secondThread() throws InterruptedException {
    Thread.sleep(2500);
    lock.lock();

    System.out.println("Press the return key...");
    new Scanner(System.in).nextLine();
    System.out.println("Got the return key.Signaling other thread");

    cond.signal();

    try {
      increment();
    } catch (Exception e) {

      e.printStackTrace();

    } finally {

      lock.unlock(); //in case exception is thrown

    }
  }

  public void finished() {

    System.out.println("Count value is " + count);
  }
}
