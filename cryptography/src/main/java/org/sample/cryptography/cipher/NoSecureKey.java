package org.sample.cryptography.cipher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoSecureKey implements Key {

  private final String keyContent;

  @Override
  public String getAlgorithm() {
    return "SimpleStringKey";
  }

  @Override
  public byte[] getEncoded() {
    return keyContent.getBytes();
  }
}
