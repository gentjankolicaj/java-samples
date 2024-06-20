package org.sample.heapleak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.sample.heapleak.halo.Spartan;
import org.sample.heapleak.halo.SpartanThread;

@Slf4j
public class Application {

  static final Random random = new Random();
  static final long MAX_INDEX = 10000000L;
  static final List<Spartan> STATIC_SPARTANS = new ArrayList<>();
  static final List<Class<? extends Spartan>> STATIC_SPARTANS_CLS = new ArrayList<>();
  static long index = 0;
  final List<Spartan> spartans = new ArrayList<>();
  final List<Class<? extends Spartan>> spartansCls = new ArrayList<>();

  public static void main(String[] args) {
    Thread halo0 = new SpartanThread("halo_0");
    Thread halo1 = new SpartanThread("halo_1");

    //Start threads
    startThreads(halo0, halo1);

    //Create app object
    //Populate with spartans
    Application application = new Application();

    trainSpartans(application);
  }

  static void trainSpartans(Application application) {
    while (index < MAX_INDEX) {
      try {
        //Allocate spartan objects
        Spartan vannak134 = new Spartan(index, "vannak", "134", "Lieutenant", "UNSC");
        Spartan riz028 = new Spartan(index + 1, "riz", "028", "Lieutenant", "UNSC");

        //Add references to static collection
        STATIC_SPARTANS.add(vannak134);
        STATIC_SPARTANS_CLS.add(vannak134.getClass());

        //Add references to instance collection
        application.spartans.add(riz028);
        application.spartansCls.add(riz028.getClass());

        //Thread sleep of app[] randomly
        int value = random.nextInt(100);
        if (value == 10) {
          Thread.sleep(3);
        }
      } catch (Exception e) {
        log.error("", e);
      } finally {
        index += 2;
      }

    }
  }

  static void startThreads(Thread... args) {
    for (Thread var : args) {
      var.start();
    }
  }

}
