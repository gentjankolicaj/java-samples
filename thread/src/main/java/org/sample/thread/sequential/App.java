package org.sample.thread.sequential;


public class App {

  public static void main(String[] args) {

    Runner logRunner = new LogRunner();
    Runner printRunner = new PrintlnRunner();

    logRunner.execute();
    printRunner.execute();

  }
}

