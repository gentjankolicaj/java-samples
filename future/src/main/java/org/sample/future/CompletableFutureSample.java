package org.sample.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureSample {

  static final String THREAD_MSG = "Thread : ";

  public static void main(String[] args) throws Exception {
    printThread();
    sample0();
    sample1();
    sample2();
    sample3();
  }

  private static void printThread() {
    log.info(String.format("%s%s", THREAD_MSG, Thread.currentThread()));
  }

  static void sample0() throws ExecutionException, InterruptedException {
    CompletableFuture<String> stringCF = new CompletableFuture<>();
    CompletableFuture.runAsync(
        () -> stringCF.complete("Testing stringCF : " + Thread.currentThread()));
    log.info(stringCF.get());
  }

  static void sample1() {
    CompletableFuture<Integer> completableFuture = create();
    completableFuture.thenAccept(CompletableFutureSample::print);
  }

  private static Integer calc() {
    printThread();
    return 3;
  }

  public static CompletableFuture<Integer> create() {
    return CompletableFuture.supplyAsync(CompletableFutureSample::calc);
  }

  public static void print(int value) {
    log.info("Value {}", value);
    printThread();
  }

  static void sample2() {
    CompletableFuture<Double> future = new CompletableFuture<>();
    future.thenApply(d -> d + 1)
        .thenApply(d -> d + 6)
        .thenAccept(d -> log.info("{}", d));
  }

  static void sample3() {
    CompletableFuture<Double> doubleCF = new CompletableFuture<>();
    doubleCF.complete(12.2);
    doubleCF.thenApply(i -> i * 3)
        .thenApply(i -> i * 3)
        .thenAccept(i -> log.info("sample3 : {}", i));

  }


}
