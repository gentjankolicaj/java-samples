package org.sample.multithread.tut_4;

public class App2 {

  public App2() {
    super();
  }


  public static void main(String[] args) {
    Worker worker = new Worker();
    worker.work4();
  }
}
