package org.sample.cryptography.impl;

public final class PseudoAutoKey implements Key {

  @Override
  public String getAlgorithm() {
    return null;
  }

  @Override
  public byte[] getEncoded() {
    return new byte[0];
  }
}
