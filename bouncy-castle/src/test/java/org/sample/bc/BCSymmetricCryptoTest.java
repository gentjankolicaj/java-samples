package org.sample.bc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.sample.bc.exception.CipherException;

@Slf4j
class BCSymmetricCryptoTest {

  @Test
  void ecbNoPadding() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    //positive test case
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("AES/ECB/NoPadding", secretKey);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("ecbNoPadding encryptedInput0 : {}", encryptedInput0);
    log.debug("ecbNoPadding decryptedInput0 : {}", new String(decryptedInput0));

    //negative test case
    IvParameterSpec iv = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    assertThatThrownBy(() -> new BCSymmetricCrypto("AES/ECB/NoPadding", secretKey, iv)).isInstanceOf(
        CipherException.class);
  }


  @Test
  void cbcNoPadding() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new BCSymmetricCrypto("AES/CBC/NoPadding", secretKey)).isInstanceOf(CipherException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("AES/CBC/NoPadding", secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcNoPadding encryptedInput0 : {}", encryptedInput0);
    log.debug("cbcNoPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void cbcPKCS5Padding() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new BCSymmetricCrypto("AES/CBC/PKCS5Padding", secretKey)).isInstanceOf(
        CipherException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("AES/CBC/PKCS5Padding", secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcPKCS5Padding encryptedInput0 : {}", encryptedInput0);
    log.debug("cbcPKCS5Padding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void cbcCTSPadding() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new BCSymmetricCrypto("AES/CBC/CTSPadding", secretKey)).isInstanceOf(
        CipherException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("AES/CBC/CTSPadding", secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcCTSPadding encryptedInput0 : {}", encryptedInput0);
    log.debug("cbcCTSPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void ctrNoPadding() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new BCSymmetricCrypto("AES/CTR/NoPadding", secretKey)).isInstanceOf(CipherException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("AES/CTR/NoPadding", secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("ctrNoPadding encryptedInput0 : {}", encryptedInput0);
    log.debug("ctrNoPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void chacha7539() {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = BCUtils.generateSecretKey("ChaCha7539");

    //negative test case
    assertThatThrownBy(() -> new BCSymmetricCrypto("ChaCha7539", secretKey)).isInstanceOf(CipherException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 12);
    BCSymmetricCrypto symmetricCrypto0 = new BCSymmetricCrypto("ChaCha7539", secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("chacha7539 encryptedInput0 : {}", encryptedInput0);
    log.debug("chacha7539 decryptedInput0 : {}", new String(decryptedInput0));
  }

}