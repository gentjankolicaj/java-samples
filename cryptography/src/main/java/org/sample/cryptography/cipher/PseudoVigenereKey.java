package org.sample.cryptography.cipher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PseudoVigenereKey implements Key {

  private final String keyContent;

  @Override
  public String getAlgorithm() {
    return "VIGENERE";
  }

  @Override
  public byte[] getEncoded() {
    return keyContent.getBytes();
  }
}
