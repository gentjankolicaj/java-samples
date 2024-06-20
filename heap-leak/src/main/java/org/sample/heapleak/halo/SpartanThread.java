package org.sample.heapleak.halo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpartanThread extends Thread {

  static final long MAX_INDEX = 10000000L;
  static final Object lock = new Object();
  static final List<Spartan> STATIC_SPARTANS = new ArrayList<>();
  static final List<Class<? extends Spartan>> STATIC_SPARTANS_CLS = new ArrayList<>();
  final Random random = new Random();
  final List<Spartan> spartans = new ArrayList<>();
  final List<Class<? extends Spartan>> spartansCls = new ArrayList<>();
  long index = 0;

  public SpartanThread(String threadName) {
    super(threadName);
  }

  @Override
  public void run() {
    while (index < MAX_INDEX) {
      try {
        Spartan john117 = new Spartan(index, "john", "117", "Master Chief Petty Officer", "UNSC");
        Spartan kai125 = new Spartan(index + 1, "kai", "125", "Petty Officer, First Class", "UNSC");

        //Add references to static collection
        //Since we have static collection & multithreading => we need object synchronization

        synchronized (lock) {
          STATIC_SPARTANS.add(john117);
          STATIC_SPARTANS_CLS.add(john117.getClass());
        }

        //Add references to instance collection
        spartans.add(kai125);
        spartansCls.add(kai125.getClass());

        int value = random.nextInt(100);
        if (value == 17) {
          Thread.sleep(3);
        }

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        index += 2;
      }
    }
  }
}
