package org.sample.cryptography.impl;

public interface Key {

  public String getAlgorithm();

  public byte[] getEncoded();

}
