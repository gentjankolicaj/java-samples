package org.sample.executors.callable;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class App {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future = executor.submit(new Processor());
        log.info("Processor result | {}", future.get());


        Future<Float> integerFuture = executor.submit(() -> 3.14f);
        log.info("Lambda result | {}", integerFuture.get());

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

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return this.getClass().getCanonicalName();
        }
    }
}
