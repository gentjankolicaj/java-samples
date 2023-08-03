package org.sample.bc;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.sample.bc.exception.CipherException;
import org.sample.bc.exception.CryptException;

@Slf4j
public class BCSymmetricCrypto {

  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";
  private final String transformation;
  private final Key key;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;
  private IvParameterSpec ivParameterSpec;


  /**
   * Block cipher modes of operation supported only :
   * <br>ECB
   *
   * @param transformation
   * @param key
   * @throws CipherException
   */
  public BCSymmetricCrypto(String transformation, Key key) throws CipherException {
    this.transformation = transformation;
    this.key = key;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, key);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, key);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param transformation
   * @param key
   * @param ivParameterSpec
   * @throws CipherException
   */
  public BCSymmetricCrypto(String transformation, Key key, IvParameterSpec ivParameterSpec) throws CipherException {
    this.transformation = transformation;
    this.key = key;
    this.ivParameterSpec = ivParameterSpec;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, key, ivParameterSpec);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, key, ivParameterSpec);
  }

  private Cipher createCipher(String transformation, int opmode, Key key, IvParameterSpec ivParameterSpec)
      throws CipherException {
    try {
      Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
      cipher.init(opmode, key, ivParameterSpec);
      return cipher;
    } catch (Exception e) {
      throw new CipherException(e);
    }

  }

  private Cipher createCipher(String transformation, int opmode, Key key) throws CipherException {
    try {
      Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
      cipher.init(opmode, key);
      return cipher;
    } catch (Exception e) {
      throw new CipherException(e);
    }

  }

  public byte[] encrypt(byte[] input) throws CryptException {
    try {
      return this.encryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptException {
    try {
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }

}
