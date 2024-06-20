package org.sample.multithread.tut_4;

public class App1 {

  public App1() {
    super();
  }


  public static void main(String[] args) {
    Worker worker2 = new Worker();
    worker2.work2(); //lists sizes printed is 0

    Worker worker3 = new Worker();
    worker3.work3(); //lists sizes printed is 2000
  }
}
