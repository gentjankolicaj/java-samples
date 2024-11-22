package org.sample.multithread.tut_14;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class App {

  public static void main(String[] args) {

    ExecutorService executor = Executors.newCachedThreadPool();

    Future<Integer> result = executor.submit(new Callable<Integer>() {

      @Override
      public Integer call() throws Exception {

        return Integer.valueOf("101");
      }

    });

    executor.shutdown();

    try {

      executor.awaitTermination(1, TimeUnit.DAYS);

    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    try {
      System.out.println("Result " + result.get());

    } catch (InterruptedException ie) {

      ie.printStackTrace();

    } catch (ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
