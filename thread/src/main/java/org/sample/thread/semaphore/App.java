package org.sample.thread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;


@Slf4j
enum Downloader {
  INSTANCE;
  private final Semaphore semaphore = new Semaphore(3, true);

  public void download() {
    try {
      semaphore.acquire();
      performDownload();
    } catch (InterruptedException e) {
      log.error("", e);
    }
    semaphore.release();
  }

  private void performDownload() {
    log.info("Downloading...");
  }
}

/**
 * Example with semaphore:
 * <br>Semaphore is used to control access to a shared resource with help of a counter variable :
 * <br>1.Signaling mechanism
 * <br>2.Process/Thread blocks itself till resource is released
 * <br>3.Maintains a set of permits
 * <br>
 * -acquire(): if a permit is available then takes it -release(): adds a permit Note: if we set fairness to true=> every single thread is
 * going to access resources
 */
@Slf4j
public class App {

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newCachedThreadPool();

    //create 20 threads to execute method download() on them.
    //Note: Since Semaphore permits=3,at any time only 3 threads are executing download out of 20 threads.
    for (int i = 0; i < 20; i++) {
      executorService.execute(Downloader.INSTANCE::download);
    }
  }
}