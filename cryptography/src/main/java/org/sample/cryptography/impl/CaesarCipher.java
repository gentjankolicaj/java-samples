package org.sample.cryptography.impl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class CaesarCipher implements Cipher {

  private static final int UTF_8_CHARSET_BASE = 1_112_064;

  public CaesarCipher() {
    log.info("Using Charset : {}", Charset.defaultCharset());
  }

  @Override
  public byte[] encrypt(byte[] plainText, Key key) throws CipherException {
    if (ArrayUtils.isEmpty(plainText)) {
      throw new IllegalArgumentException("Plaintext can't be empty.");
    }

    if (Objects.isNull(key) || ArrayUtils.isEmpty(key.getEncoded())) {
      throw new IllegalArgumentException("Key can't be null or empty");
    }
    int shift = ByteBuffer.wrap(key.getEncoded()).getInt();
    byte[] cipherText = new byte[plainText.length];
    for (int i = 0; i < plainText.length; i++) {
      byte p = plainText[i];
      int ciphered = (p + shift) % UTF_8_CHARSET_BASE;
      cipherText[i] = (byte) ciphered;
    }
    return cipherText;
  }

  @Override
  public byte[] decrypt(byte[] cipherText, Key key) throws CipherException {
    if (ArrayUtils.isEmpty(cipherText)) {
      throw new IllegalArgumentException("Plaintext can't be empty.");
    }

    if (Objects.isNull(key) || ArrayUtils.isEmpty(key.getEncoded())) {
      throw new IllegalArgumentException("Key can't be null or empty");
    }

    int shift = ByteBuffer.wrap(key.getEncoded()).getInt();
    byte[] plainText = new byte[cipherText.length];
    for (int i = 0; i < cipherText.length; i++) {
      byte c = cipherText[i];
      int plain = (c - shift) % UTF_8_CHARSET_BASE;
      plainText[i] = (byte) plain;
    }
    return plainText;
  }
}
