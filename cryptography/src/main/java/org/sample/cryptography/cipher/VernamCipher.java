package org.sample.cryptography.cipher;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Vernam cipher implementation, works only for UTF-8.
 * <br>For more check <a href="https://en.wikipedia.org/wiki/Gilbert_Vernam">Gilbert Vernam</a>
 * <br>Encryption : ( plaintext XOR key ) mod(charset-size) => ciphertext
 * <br>Decryption : ( ciphertext XOR key ) mod(charset-size) => ciphertext
 */
@Slf4j
public final class VernamCipher implements Cipher {

  private static final int UTF_8_CHARSET_SIZE = 1_112_064;

  public VernamCipher() {
    log.warn("Vernam cipher implemented for charset : {}", Charset.defaultCharset());
  }

  @Override
  public byte[] encrypt(byte[] plainText, Key key) throws CipherException {
    if (ArrayUtils.isEmpty(plainText)) {
      throw new IllegalArgumentException("Plaintext can't be empty");
    }
    if (Objects.isNull(key) || ArrayUtils.isEmpty(key.getEncoded())) {
      throw new IllegalArgumentException("Key can't be null or empty");
    }
    if (plainText.length != key.getEncoded().length) {
      throw new IllegalArgumentException(
          "Vernam cipher require plaintext length equal to key length");
    }
    byte[] cipherText = new byte[plainText.length];
    byte[] keyContent = key.getEncoded();
    int key4ByteCharsNumber = keyContent.length / 4;
    for (int i = 0; i < plainText.length; i++) {
      byte plainChar = plainText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(keyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte cipherChar = (byte) ((plainChar ^ keyChar) % UTF_8_CHARSET_SIZE);
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

    if (cipherText.length != key.getEncoded().length) {
      throw new IllegalArgumentException(
          "Vernam cipher require plaintext length equal to key length");
    }
    byte[] plainText = new byte[cipherText.length];
    byte[] keyContent = key.getEncoded();
    int key4ByteCharsNumber = keyContent.length / 4;
    for (int i = 0; i < cipherText.length; i++) {
      byte cipherChar = cipherText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(keyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte plainChar = (byte) ((cipherChar ^ keyChar) % UTF_8_CHARSET_SIZE);
      plainText[i] = plainChar;
    }
    return plainText;
  }
}
