package org.sample.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.sample.cryptography.cipher.TabulaRecta;


@Slf4j
public class TabulaRectaSample {

  public static void main(String[] args) {
    TabulaRecta.printTable(3, 31, 127);
  }

}
