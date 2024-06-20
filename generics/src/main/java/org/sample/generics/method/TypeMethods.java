package org.sample.generics.method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeMethods<T> {

  T t;

  public void print0(T t) {
    log.info("{}", t);
  }

  public T print1(T t) {
    return t;
  }
}
