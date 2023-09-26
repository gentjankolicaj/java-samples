package org.sample.cryptography.cipher;


public interface Cipher {

  byte[] encrypt(byte[] plainText, Key key) throws CipherException;

  byte[] decrypt(byte[] cipherText, Key key) throws CipherException;

}
