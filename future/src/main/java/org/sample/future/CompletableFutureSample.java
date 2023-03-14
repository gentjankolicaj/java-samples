package org.sample.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class CompletableFutureSample {

    static final String THREAD_MSG = "Thread : ";

    public static void main(String[] args) throws Exception {
        log.info(THREAD_MSG + Thread.currentThread());
        sample0();
        sample1();
        sample2();
        sample3();
    }

    static void sample0() throws ExecutionException, InterruptedException {
        CompletableFuture<String> stringCF = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> stringCF.complete("Testing stringCF : " + Thread.currentThread()));
        log.info(stringCF.get());
    }

    static void sample1() {
        CompletableFuture<Integer> completableFuture = create();
        completableFuture.thenAccept(CompletableFutureSample::print);
    }

    private static Integer calc() {
        log.info(THREAD_MSG + Thread.currentThread());
        return 3;
    }

    public static CompletableFuture<Integer> create() {
        return CompletableFuture.supplyAsync(CompletableFutureSample::calc);
    }

    public static void print(int value) {
        log.info("Value " + value);
        log.info(THREAD_MSG + Thread.currentThread());
    }

    static void sample2() {
        CompletableFuture<Double> future = new CompletableFuture<>();
        future.thenApply(d -> d + 1)
                .thenApply(d -> d + 6)
                .thenAccept(d -> log.info("{}", d));
    }

    static void sample3() throws Exception {
        CompletableFuture<Double> doubleCF = new CompletableFuture<>();
        doubleCF.complete(12.2);
        doubleCF.thenApply(i -> i * 3)
                .thenApply(i -> i * 3)
                .thenAccept(i -> log.info("sample3 : {}", i));

    }


}
