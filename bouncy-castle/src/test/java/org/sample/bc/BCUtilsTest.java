package org.sample.bc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

@Slf4j
class BCUtilsTest {

  @Test
  void logSetup() {
    BCUtils.logSetup();
  }

  @Test
  void loadRSAPrivateKeyPem() {
    Path privateKeyPem = Path.of(
        Thread.currentThread().getContextClassLoader().getResource("private-key.pem").getPath());
    Optional<RSAPrivateKey> optional = BCUtils.loadRSAPrivateKeyPem(privateKeyPem);
    assertThat(optional.get()).isNotNull();

  }

  @Test
  void loadRSAPublicKeyPem() {
    Path publicKeyPem = Path.of(Thread.currentThread().getContextClassLoader().getResource("public-key.pem").getPath());
    Optional<RSAPublicKey> optional = BCUtils.loadRSAPublicKeyPem(publicKeyPem);
    assertThat(optional.get()).isNotNull();
  }

  @Test
  void loadX509Certificate() throws FileNotFoundException, CertificateException {
    Path certPath = Path.of(Thread.currentThread().getContextClassLoader().getResource("bc.cer").getPath());
    X509Certificate x509Certificate = BCUtils.loadX509Certificate(certPath);
    assertThat(x509Certificate).isNotNull();
  }

  @Test
  void generateIvParameterSpec() throws GeneralSecurityException {
    assertThat(BCUtils.generateIvParameterSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void generateSecretKey() throws GeneralSecurityException {
    assertThat(BCUtils.generateSecretKey("HmacSHA256")).isNotNull();
    assertThat(BCUtils.generateSecretKey("AES")).isNotNull();
  }

  @Test
  void createSecretKeySpec() {
    byte[] keyBytes = Hex.decode("2ccd85dfc8d18cb5d84fef4b198554679fece6e8692c9147b0da983f5b7bd417");
    assertThat(BCUtils.createSecretKeySpec("HmacSHA256", keyBytes)).isNotNull();
  }

  @Test
  void computeDigest() throws GeneralSecurityException {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    assertThat(BCUtils.computeDigest("SHA-256", input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("SHA-256 size {} : {} ", BCUtils.computeDigest("SHA-256", input.getBytes()).length,
        BCUtils.computeDigest("SHA-256", input.getBytes()));

    assertThatThrownBy(() -> BCUtils.computeDigest("SHA-256", null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createDigestCalculator() throws IOException, GeneralSecurityException, OperatorException {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] digestedInput = BCUtils.computeDigest("SHA-256", input.getBytes());

    assertThat(BCUtils.createDigestCalculator("SHA-256")).isNotNull();

    DigestCalculator digestCalculator = BCUtils.createDigestCalculator("SHA-256");
    OutputStream os = digestCalculator.getOutputStream();

    //write input to digest calculator
    os.write(input.getBytes());
    os.close();

    //digest input & assert
    assertThat(digestCalculator.getDigest()).containsExactly(digestedInput);
  }

  @Test
  void computeMac() throws GeneralSecurityException {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";

    //HMAC test
    SecretKey hmacKey = BCUtils.generateSecretKey("HmacSHA256");
    assertThat(BCUtils.computeMac("HmacSHA256", hmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}", BCUtils.computeMac("HmacSHA256", hmacKey, input.getBytes()).length);

    //AESCMAC test
    SecretKey cmacKey = BCUtils.generateSecretKey("AES");
    assertThat(BCUtils.computeMac("AESCMAC", cmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}", BCUtils.computeMac("AESCMAC", cmacKey, input.getBytes()).length);
  }

  @Test
  void getAlgorithmParamGen() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = BCUtils.getAlgorithmParamGen("GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    BCSymmetricCrypto bcSymmetricCrypto = new BCSymmetricCrypto("AES/GCM/NoPadding", secretKey, algParamsGCM);
    byte[] encryptedInput0 = bcSymmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = bcSymmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("GCM encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("GCM decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void generateAlgorithmParameters() throws GeneralSecurityException {
    AlgorithmParameters algorithmParameters = BCUtils.generateAlgorithmParameters("CCM");

    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = BCUtils.generateSecretKey("AES");

    BCSymmetricCrypto bcSymmetricCrypto = new BCSymmetricCrypto("AES/CCM/NoPadding", secretKey, algorithmParameters);
    byte[] encryptedInput0 = bcSymmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = bcSymmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("GCM encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("GCM decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void rsaGenerateKeyPair() throws GeneralSecurityException {
    KeyPair keyPair0 = BCUtils.generateKeyPair("RSA", 2048);
    assertThat(keyPair0).isNotNull();
    assertThat(keyPair0.getPrivate()).isNotNull();
    assertThat(keyPair0.getPublic()).isNotNull();

  }

  @Test
  void generatePKCS5Scheme2() {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(BCUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(), 256)).isNotNull();

    //hasSize(32) because returns byte[] and 256/8=32 bytes length
    assertThat(BCUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(), 256)).hasSize(32);
  }

  @Test
  void generateSCRYPT() {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(BCUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 16)).isNotNull().hasSize(16);
    assertThat(BCUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 32)).isNotNull().hasSize(32);
    assertThat(BCUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 256)).isNotNull().hasSize(256);
    log.debug("generateSCRYPT() : {}", new String(BCUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 64)));
  }
}