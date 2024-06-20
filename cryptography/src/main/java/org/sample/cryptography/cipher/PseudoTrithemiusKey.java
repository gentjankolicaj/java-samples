package org.sample.cryptography.cipher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PseudoTrithemiusKey implements Key {

  private final String keyContent;

  @Override
  public String getAlgorithm() {
    return "TRITHEMIUS";
  }

  @Override
  public byte[] getEncoded() {
    return keyContent.getBytes();
  }
}
