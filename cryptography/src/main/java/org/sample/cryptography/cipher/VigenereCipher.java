package org.sample.cryptography.cipher;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Vigenere cipher implementation, works only for UTF-8.
 * <br>For more check <a href="https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher">Vigenere
 * cipher</a>
 * <br>Encryption : ( plaintext + key ) mod(charset-size) => ciphertext
 * <br>Decryption : ( ciphertext - key ) mod(charset-size) => plaintext
 */
@Slf4j
public final class VigenereCipher implements Cipher {

  private static final int UTF_8_CHARSET_SIZE = 1_112_064;

  public VigenereCipher() {
    log.warn("Vigenere cipher implemented for charset : {}", Charset.defaultCharset());
  }


  @Override
  public byte[] encrypt(byte[] plainText, Key key) throws CipherException {
    if (ArrayUtils.isEmpty(plainText)) {
      throw new IllegalArgumentException("Plaintext can't be empty");
    }
    if (Objects.isNull(key) || ArrayUtils.isEmpty(key.getEncoded())) {
      throw new IllegalArgumentException("Key can't be null or empty");
    }
    byte[] cipherText = new byte[plainText.length];
    byte[] keyContent = key.getEncoded();
    int key4ByteCharsNumber = keyContent.length / 4;
    for (int i = 0; i < plainText.length; i++) {
      byte plainChar = plainText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(keyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte cipherChar = (byte) ((plainChar + keyChar) % UTF_8_CHARSET_SIZE);
      cipherText[i] = cipherChar;
    }
    return cipherText;
  }

  @Override
  public byte[] decrypt(byte[] cipherText, Key key) throws CipherException {
    if (ArrayUtils.isEmpty(cipherText)) {
      throw new IllegalArgumentException("Plaintext can't be empty");
    }
    if (Objects.isNull(key) || ArrayUtils.isEmpty(key.getEncoded())) {
      throw new IllegalArgumentException("Key can't be null or empty");
    }
    byte[] plainText = new byte[cipherText.length];
    byte[] keyContent = key.getEncoded();
    int key4ByteCharsNumber = keyContent.length / 4;
    for (int i = 0; i < cipherText.length; i++) {
      byte cipherChar = cipherText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(keyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte plainChar = (byte) ((cipherChar - keyChar) % UTF_8_CHARSET_SIZE);
      plainText[i] = plainChar;
    }
    return plainText;
  }
}
