package org.sample.bc;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.sample.bc.exception.CryptException;

@Slf4j
@Getter
public class BCSymmetricCrypto {

  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";
  private final String transformation;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;
  private AlgorithmParameterSpec algorithmParameterSpec;
  private AlgorithmParameters algorithmParameters;


  /**
   * Block cipher modes of operation supported only :
   * <br>ECB
   *
   * @param transformation cipher transformation
   * @param key            symmetric key
   * @throws GeneralSecurityException wrapper exception
   */
  public BCSymmetricCrypto(String transformation, Key key) throws GeneralSecurityException {
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, key);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, key);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param transformation         cipher transformation
   * @param key                    symmetric key
   * @param algorithmParameterSpec algorithm parameter specs at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public BCSymmetricCrypto(String transformation, Key key,
      AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    this.transformation = transformation;
    this.algorithmParameterSpec = algorithmParameterSpec;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, key,
        algorithmParameterSpec);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, key,
        algorithmParameterSpec);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param transformation      cipher transformation
   * @param key                 symmetric key
   * @param algorithmParameters algorithm parameters at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public BCSymmetricCrypto(String transformation, Key key, AlgorithmParameters algorithmParameters)
      throws GeneralSecurityException {
    this.transformation = transformation;
    this.algorithmParameters = algorithmParameters;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, key,
        algorithmParameters);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, key,
        algorithmParameters);
  }


  private Cipher createCipher(String transformation, int opmode, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
    cipher.init(opmode, key);
    return cipher;
  }

  private Cipher createCipher(String transformation, int opmode, Key key,
      AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
    cipher.init(opmode, key, algorithmParameterSpec);
    return cipher;

  }

  private Cipher createCipher(String transformation, int opmode, Key key,
      AlgorithmParameters algorithmParameters)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
    cipher.init(opmode, key, algorithmParameters);
    return cipher;

  }

  public byte[] encrypt(byte[] input) throws CryptException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.encryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }

}
