package org.sample.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  public static void main(String[] args) throws InterruptedException {
    final AtomicInteger atomicInteger = new AtomicInteger();
    new Thread(atomicInteger::incrementAndGet, "thread1").start();
    new Thread(atomicInteger::incrementAndGet, "thread2").start();

    final AtomicLong atomicLong = new AtomicLong();
    new Thread(atomicLong::incrementAndGet, "thread3").start();
    new Thread(atomicLong::incrementAndGet, "thread4").start();

    //An arbitrary wait for threads to terminate
    Thread.sleep(2000);
    log.info("AtomicInteger value {}", atomicInteger.get());
    log.info("AtomicLong value {}", atomicLong.get());

  }
}
