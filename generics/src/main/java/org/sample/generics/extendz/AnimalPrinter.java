package org.sample.generics.extendz;

import lombok.extern.slf4j.Slf4j;
import org.sample.generics.core.Animal;

@Slf4j
public class AnimalPrinter<T extends Animal> {

  public void print(T t) {
    log.info("{}", t);
  }
}
