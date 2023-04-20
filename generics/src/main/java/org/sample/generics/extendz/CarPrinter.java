package org.sample.generics.extendz;

import lombok.extern.slf4j.Slf4j;
import org.sample.generics.core.Car;

//Every type must be child of Car class
@Slf4j
public class CarPrinter<T extends Car> {

  public void print(T t) {
    log.info("{}", t);
  }
}
