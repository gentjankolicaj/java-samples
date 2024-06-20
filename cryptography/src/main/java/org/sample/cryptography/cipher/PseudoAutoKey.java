package org.sample.cryptography.cipher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PseudoAutoKey implements Key {

  private final String primer;

  @Override
  public String getAlgorithm() {
    return "AUTOKEY";
  }

  @Override
  public byte[] getEncoded() {
    return primer.getBytes();
  }
}
