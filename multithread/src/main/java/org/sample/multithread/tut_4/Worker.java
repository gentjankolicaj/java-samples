package org.sample.multithread.tut_4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Every java object has only one intrinsic lock that can be aquired by one thread alone
public class Worker {

  private final Random random = new Random();

  private final List<Integer> list1 = new ArrayList<>();
  private final List<Integer> list2 = new ArrayList<>();


  public synchronized void stageOne() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    list1.add(random.nextInt());
  }

  public synchronized void stageTwo() {

    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    list2.add(random.nextInt());
  }

  public void process() {
    System.out.println("Process started.");
    for (short i = 0; i < 1000; i++) {
      stageOne();
      stageTwo();
    }

    System.out.println("Process ended.");

  }

  public void work() {
    System.out.println("Starting...");
    long startTime = System.currentTimeMillis();
    process();
    long endTime = System.currentTimeMillis();
    long spentTime = endTime - startTime;
    System.out.println("Ended " + spentTime + " milis.");
    System.out.println("List1 size :" + list1.size() + ",List2 size :" + list2.size());
  }

  public void work2() { //size list is 0 because i start thread exe,and imidiatly pass to execute next line of code
    System.out.println();
    System.out.println("Starting...");
    long startTime = System.currentTimeMillis();
    new Thread(new Runnable() {
      public void run() {
        process();
      }
    }).start();
    long endTime = System.currentTimeMillis();
    long spentTime = endTime - startTime;
    System.out.println("Ended " + spentTime + " milis.");
    System.out.println("List1 size :" + list1.size() + ",List2 size :" + list2.size());
  }

  public void work3() {//Size list is 2000,because by calling method join I wait until thread finishes executing method run and then passes to execution of below lines of code
    System.out.println();
    System.out.println("Starting...");
    long startTime = System.currentTimeMillis();
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        process();
      }
    });

    t1.start();

    try {

      t1.join();

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    long spentTime = endTime - startTime;
    System.out.println("Ended " + spentTime + " milis.");
    System.out.println("List1 size :" + list1.size() + ",List2 size :" + list2.size());
  }

  public void work4() {//Size list is 2000,because by calling method join I wait until thread finishes executing method run and then passes to execution of below lines of code
    System.out.println();
    System.out.println("Starting...");
    long startTime = System.currentTimeMillis();
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        process();
      }
    });

    Thread t2 = new Thread(new Runnable() {
      public void run() {
        process();
      }
    });

    t1.start();
    t2.start();

    try {

      t1.join();
      t2.join();

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    long spentTime = endTime - startTime;
    System.out.println("Ended " + spentTime + " milis.");
    System.out.println("List1 size :" + list1.size() + ",List2 size :" + list2.size());
  }
}
