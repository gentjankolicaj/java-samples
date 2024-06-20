package org.sample.cryptography.cipher;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Autokey cipher implementation, works only for UTF-8.
 * <br> For more check : <a href="https://en.wikipedia.org/wiki/Autokey_cipher">Autokey cipher</a>
 */
@Slf4j
public final class AutokeyCipher implements Cipher {

  private static final int UTF_8_CHARSET_SIZE = 1_112_064;

  public AutokeyCipher() {
    log.warn("Autokey cipher implemented for charset : {}", Charset.defaultCharset());
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
    byte[] primer = key.getEncoded();

    //concatenate primer & message
    byte[] finalKeyContent = Arrays.copyOf(primer, primer.length + plainText.length);
    System.arraycopy(plainText, 0, finalKeyContent, primer.length, plainText.length);

    //Total chars encoded in 4 bytes
    int key4ByteCharsNumber = finalKeyContent.length / 4;

    for (int i = 0; i < plainText.length; i++) {
      byte plainChar = plainText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(finalKeyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte cipherChar = (byte) TabulaRecta.getCrossCharacterDec(plainChar, keyChar);
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
    byte[] primer = key.getEncoded();

    //concatenate primer & message
    byte[] finalKeyContent = Arrays.copyOf(primer, primer.length + plainText.length);
    System.arraycopy(plainText, 0, finalKeyContent, primer.length, plainText.length);

    //Total chars encoded in 4 bytes
    int key4ByteCharsNumber = finalKeyContent.length / 4;
    for (int i = 0; i < cipherText.length; i++) {
      byte cipherChar = cipherText[i];
      byte[] keyCharacterArray = Arrays.copyOfRange(finalKeyContent, i % key4ByteCharsNumber,
          (i % key4ByteCharsNumber) + 4);
      int keyChar = ByteBuffer.wrap(keyCharacterArray).getInt();
      byte plainChar = (byte) ((cipherChar - keyChar) % UTF_8_CHARSET_SIZE);
      plainText[i] = plainChar;
    }
    return plainText;
  }
}
