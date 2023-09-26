package org.sample.cryptography.cipher;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PseudoCaesarKey implements Key {

  private final int shift;

  @Override
  public String getAlgorithm() {
    return "CAESAR";
  }

  @Override
  public byte[] getEncoded() {
    byte[] array = new byte[4];
    array[0] = (byte) (shift >> 24);
    array[1] = (byte) (shift >> 18);
    array[2] = (byte) (shift >> 10);
    array[3] = (byte) shift;
    return array;
  }
}
