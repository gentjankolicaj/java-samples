package org.sample.vthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

@Slf4j
public class ForkJoinTasks {

    public static void main(String[] args) {
        ForkJoinTask task = ForkJoinPool.commonPool().submit(() -> log.info("{}", Thread.currentThread()));
        task.join();
    }
}
