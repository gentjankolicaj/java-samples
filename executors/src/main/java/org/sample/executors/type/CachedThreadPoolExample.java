package org.sample.executors.type;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <br>CachedThreadPool is :
 * <br>1.One of 4 types of executors
 * <br>2.This executor has number of threads not bounded, we can execute tasks/operations in concurrent manner
 * <br>3.If all threads are busy and new task comes in,pool will create & add new thread to executor.
 * <br>4.If thread is idle for 60 seconds , it is removed
 * <br>5.it is used for short parallel task
 */
@Slf4j
public class CachedThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executor.execute(new Task(i));
        }

    }

    static class Task implements Runnable {
        int id;

        Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            log.info("Executing task {} on thread : {}", id, Thread.currentThread().getId());
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
