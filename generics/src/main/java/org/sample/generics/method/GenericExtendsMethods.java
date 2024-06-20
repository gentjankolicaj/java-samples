package org.sample.generics.method;

import lombok.extern.slf4j.Slf4j;
import org.sample.generics.core.Animal;
import org.sample.generics.core.Car;


@Slf4j
public class GenericExtendsMethods {


  public static <T extends Animal> T processAnimal0(T t) {
    log.info(
        "This static method has T extends Animal for (return & param) " + t + " , " + t.getClass());
    return t;
  }

  public static <T extends Car> T processCar0(T t) {
    log.info(
        "This static method has T extends Car for (return & param) " + t + " , " + t.getClass());
    return t;
  }

  public <T extends Animal> T processAnimal1(T t) {
    log.info("This instance method has T extends Animal for (return & param) " + t + " , "
        + t.getClass());
    return t;
  }

  public <T extends Car> T processCar1(T t) {
    log.info(
        "This instance method has T extends Car for (return & param) " + t + " , " + t.getClass());
    return t;
  }


}
