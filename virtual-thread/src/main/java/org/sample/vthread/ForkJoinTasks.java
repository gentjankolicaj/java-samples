package org.sample.vthread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ForkJoinTasks {

  public static void main(String[] args) {
    ForkJoinTask task = ForkJoinPool.commonPool()
        .submit(() -> log.info("{}", Thread.currentThread()));
    task.join();
  }
}
