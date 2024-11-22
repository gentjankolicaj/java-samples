package org.sample.multithread.tut_4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyLocks {

  private final Random random = new Random();

  private final Object lock1 = new Object();
  private final Object lock2 = new Object();

  private final List<Integer> list1 = new ArrayList<>();
  private final List<Integer> list2 = new ArrayList<>();


  public MyLocks() {
    super();
    // TODO Auto-generated constructor stub
  }


  public void stageOne() {
    synchronized (lock1) { //synchronize varible lock1,means I get intrinsic lock for that object of that variable
      //and also no multiple threads can access object lock1 and varibles synchronized block

      try {

        Thread.sleep(5000);

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      list1.add(random.nextInt());
    }
  }


  public void stageTwo() {
    synchronized (lock2) { //synchronize varible lock2,means I get intrinsic lock for that object of that variable
      //and also no multiple threads can access object lock2 and variables in synchronized block

      try {

        Thread.sleep(5000);

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      list2.add(random.nextInt());
    }
  }


}
