package org.sample.cryptography.cipher;

public interface Key {

  String getAlgorithm();

  byte[] getEncoded();

}
