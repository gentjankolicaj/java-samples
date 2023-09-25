package org.sample.cryptography.impl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AutokeyCipher implements Cipher {


  @Override
  public byte[] encrypt(byte[] plainText, Key key) throws CipherException {
    return new byte[0];
  }

  @Override
  public byte[] decrypt(byte[] cipherText, Key key) throws CipherException {
    return new byte[0];
  }
}
