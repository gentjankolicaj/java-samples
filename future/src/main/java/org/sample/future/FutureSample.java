package org.sample.future;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class FutureSample {
    static final ExecutorService executorService = Executors.newWorkStealingPool();

    public static void main(String[] args) {
        int a = 0, b = 11;
        Callable<Integer> add = () -> {
            return a + b;
        };
        Callable<Integer> subtract = () -> {
            return a - b;
        };

        Future<Integer> futureAdd = executorService.submit(add);
        Future<Integer> futureSubtract = executorService.submit(subtract);

        delayInSeconds(10);
    }

    static void delayInSeconds(int delay) {
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
