package org.sample.multithreading.sequential;

class PrintlnRunner implements Runner {

  public void execute() {
    for (int i = 0; i < 100; i++) {
      System.out.println("PrintlnRunner : " + i);
    }
  }
}
