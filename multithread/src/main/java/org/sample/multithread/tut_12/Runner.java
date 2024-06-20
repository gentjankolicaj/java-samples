package org.sample.multithread.tut_12;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {

  private final Account acc1 = new Account();
  private final Account acc2 = new Account();

  private final Lock lock1 = new ReentrantLock();
  private final Lock lock2 = new ReentrantLock();

  public static void transfer(Account from, Account to, int amount) {
    int fromBalance = from.getBalance();
    from.setBalance(fromBalance - amount);
    int toBalance = to.getBalance();
    to.setBalance(toBalance + amount);
  }

  // In order to avoid README.md i write this method
  public void acquireLocks(Lock first, Lock second) throws InterruptedException {

    while (true) {
      boolean gotFirstLock = false;
      boolean gotSecondLock = false;

      try {
        gotFirstLock = first.tryLock();
        gotSecondLock = second.tryLock();

      } catch (Exception e) {
        log.error("", e);
      } finally {

        if (gotFirstLock && gotSecondLock) {
          return;
        }

        if (gotFirstLock) {
          first.unlock();
        }

        if (gotSecondLock) {
          second.unlock();
        }

      }
    }
  }

  public void firstThread() throws InterruptedException {
    Random random = new Random();
    for (int i = 0; i < 10000; i++) {

      // In order for thread to call secondThread method thread must aquire both locks
      acquireLocks(lock1, lock2);

      try {

        transfer(acc1, acc2, random.nextInt(100));

      } catch (Exception e) {
        log.error("", e);
      } finally {

        lock1.unlock();
        lock2.unlock();

      }
    }
  }

  public void secondThread() throws InterruptedException {
    Random random = new Random();
    for (int i = 0; i < 10000; i++) {

      // In order for thread to call secondThread method thread must aquire both locks
      acquireLocks(lock1, lock2);

      try {

        transfer(acc1, acc2, random.nextInt(100));

      } catch (Exception e) {
        log.error("", e);
      } finally {

        lock1.unlock();
        lock2.unlock();

      }
    }
  }

  public void finished() {
    log.info("Account 1:{}", acc1.getBalance());
    log.info("Account 2:{}", acc2.getBalance());
    log.info("Total balance :{}", acc1.getBalance() + acc2.getBalance());
  }
}
