package org.sample.cryptography.cipher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PseudoVernamKey implements Key {

  private final String keyContent;

  @Override
  public String getAlgorithm() {
    return "VERNAM";
  }

  @Override
  public byte[] getEncoded() {
    return keyContent.getBytes();
  }
}
