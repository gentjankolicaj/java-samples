package org.sample.ccollections.inconsistency;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * This example show the problem that arises when multiple threads perform operations on un-synchronized collections. Problems : 1.Race
 * condition 2.Inconsistency Note: At this example somehow threads are not starting in parallel & list is consistent when I run
 * locally.Usually an exception would be thrown.
 */
@Slf4j
public class App {

  public static void main(String[] args) {
    List<Integer> ints = new ArrayList<>();

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        ints.add(i);
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        ints.add(i);
      }
    });

    //by calling start() , threads state changes new=>active
    t1.start();
    t2.start();

    //wait for thread to finish executions
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      log.error("", e);
      Thread.currentThread().interrupt();
    }

    log.info("List 'ints' size {},\n contents {}", ints.size(), ints);
  }

}
