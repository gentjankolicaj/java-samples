package org.sample.generics.type;

import lombok.extern.slf4j.Slf4j;

//In java type parameters are defined <T>
@Slf4j
public class TypePrinter<T> {

  public void print(T t) {
    log.info("{}", t);
  }
}
