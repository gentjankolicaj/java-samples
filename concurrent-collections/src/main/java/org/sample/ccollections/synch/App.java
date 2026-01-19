package org.sample.ccollections.synch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * This example shows creation of synchronized list using Collections.class. Some methods of Collections class for synchronized
 * collections:
 * <br> 1.synchronizedCollection(T t) where T is of type Collection
 * <br>2.synchronizedSet(T t) where T is of type Set
 * <br>3.synchronizedList(T t) where T is of type Lis
 * <br>4.synchronizedMap(T t) where T is of type Map
 */
@Slf4j
public class App {

  public static void main(String[] args) {
    //add() & remove() are synchronized
    //Intrinsic lock on collection
    List<Integer> ints = Collections.synchronizedList(new ArrayList<>());

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
