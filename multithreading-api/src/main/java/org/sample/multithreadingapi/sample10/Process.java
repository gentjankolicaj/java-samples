package org.sample.multithreadingapi.sample10;

import java.util.LinkedList;

public class Process {

  private final int LIMIT = 10;
  private final LinkedList<Integer> list = new LinkedList<Integer>();
  private final Object lock = new Object();// I use this object intrinsic lock to synchronize access

  public void produce() throws InterruptedException {

    int value = 0;

    while (true) {

      synchronized (lock) {

        while (list.size() == LIMIT) {
          lock.wait();
        }

        list.add(value++);
        lock.notify();

      }

    }

  }

  public void consume() throws InterruptedException {

    while (true) {

      synchronized (lock) {

        while (list.size() == 0) {
          lock.wait();
        }

        int value = list.removeFirst(); // FIFO
        System.out.println("Value :" + value + ",list size:" + list.size());

        lock.notify();

      }

      Thread.sleep(1000);
    }
  }
}
