package org.sample.executors.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Callable interface : We usually use Callable interface when : 1.We want to return a result after
 * thread terminated. 2.We want to execute task concurrently but retrieve results
 */
@Slf4j
public class CallableExample1 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(2);

    List<Future<String>> list = new ArrayList<>();

    for (int i = 0; i < 20; i++) {
      Future<String> future = executor.submit(new Processor(i));
      list.add(future);
    }

    //Note: Future.get() wait if necessary for computation to finish & retrieve result
    for (Future<String> future : list) {
      log.info("Processor result | {}", future.get());
    }

    //send shutdown request & stop new task
    executor.shutdown();

    try {
      if (executor.awaitTermination(3, TimeUnit.SECONDS)) {
        //immediate attempt shutdown with shutdownNow()
      }
    } catch (InterruptedException e) {
      log.error("", e);
      executor.shutdownNow();
    }

  }

  static class Processor implements Callable<String> {

    int id;

    Processor(int id) {
      this.id = id;
    }

    @Override
    public String call() throws Exception {
      Thread.sleep(1000);
      return "|" + this.getClass().getCanonicalName();
    }
  }
}
