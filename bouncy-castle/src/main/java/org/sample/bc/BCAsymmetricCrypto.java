package org.sample.bc;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import javax.crypto.Cipher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.sample.bc.exception.CryptException;

@Slf4j
@Getter
public class BCAsymmetricCrypto {

  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";
  private final String transformation;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;

  /**
   * Instance is used for encrypt/decrypt with public-private keys.
   *
   * @param transformation cipher transformation
   * @param encryptKey     encryption key (public | private key)
   * @param decryptKey     decryption key (public | private key)
   * @throws GeneralSecurityException wrapper exception
   */
  public BCAsymmetricCrypto(String transformation, Key encryptKey, Key decryptKey)
      throws GeneralSecurityException {
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, encryptKey);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, decryptKey);
  }

  /**
   * Instance is used for encrypt/decrypt with public-private keys.Invoking this constructor sets up
   * for encrypting with private-key and decrypting with public-key.
   *
   * @param transformation cipher transformation
   * @param keyPair        public-private key pair
   * @throws GeneralSecurityException wrapper exception
   */
  public BCAsymmetricCrypto(String transformation, KeyPair keyPair)
      throws GeneralSecurityException {
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, keyPair.getPrivate());
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, keyPair.getPublic());
  }

  private Cipher createCipher(String transformation, int opmode, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, BC_SECURITY_PROVIDER);
    cipher.init(opmode, key);
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
