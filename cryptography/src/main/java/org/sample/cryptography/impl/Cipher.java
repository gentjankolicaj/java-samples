package org.sample.cryptography.impl;


public interface Cipher {

  public byte[] encrypt(byte[] plainText, Key key) throws CipherException;

  public byte[] decrypt(byte[] cipherText, Key key) throws CipherException;

}
