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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

@Slf4j
class BCJceUtilsTest {

  @Test
  void logSetup() {
    BCJceUtils.logSetup();
  }

  @Test
  void loadRSAPrivateKeyPem() {
    Path privateKeyPem = Path.of(
        Thread.currentThread().getContextClassLoader().getResource("private-key.pem").getPath());
    Optional<RSAPrivateKey> optional = BCJceUtils.loadRSAPrivateKeyPem(privateKeyPem);
    assertThat(optional.get()).isNotNull();

  }

  @Test
  void loadRSAPublicKeyPem() {
    Path publicKeyPem = Path.of(Thread.currentThread().getContextClassLoader().getResource("public-key.pem").getPath());
    Optional<RSAPublicKey> optional = BCJceUtils.loadRSAPublicKeyPem(publicKeyPem);
    assertThat(optional.get()).isNotNull();
  }

  @Test
  void loadX509Certificate() throws FileNotFoundException, CertificateException {
    Path certPath = Path.of(Thread.currentThread().getContextClassLoader().getResource("bc.cer").getPath());
    X509Certificate x509Certificate = BCJceUtils.loadX509Certificate(certPath);
    assertThat(x509Certificate).isNotNull();
  }

  @Test
  void generateIvParameterSpec() throws GeneralSecurityException {
    assertThat(BCJceUtils.generateIvParameterSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void generateSecretKey() throws GeneralSecurityException {
    assertThat(BCJceUtils.generateSecretKey("HmacSHA256")).isNotNull();
    assertThat(BCJceUtils.generateSecretKey("AES")).isNotNull();
  }

  @Test
  void createSecretKeySpec() {
    byte[] keyBytes = Hex.decode("2ccd85dfc8d18cb5d84fef4b198554679fece6e8692c9147b0da983f5b7bd417");
    assertThat(BCJceUtils.createSecretKeySpec("HmacSHA256", keyBytes)).isNotNull();
  }

  @Test
  void computeDigest() throws GeneralSecurityException {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    assertThat(BCJceUtils.computeDigest("SHA-256", input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("SHA-256 size {} : {} ", BCJceUtils.computeDigest("SHA-256", input.getBytes()).length,
        BCJceUtils.computeDigest("SHA-256", input.getBytes()));

    assertThatThrownBy(() -> BCJceUtils.computeDigest("SHA-256", null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createDigestCalculator() throws IOException, GeneralSecurityException, OperatorException {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] digestedInput = BCJceUtils.computeDigest("SHA-256", input.getBytes());

    assertThat(BCJceUtils.createDigestCalculator("SHA-256")).isNotNull();

    DigestCalculator digestCalculator = BCJceUtils.createDigestCalculator("SHA-256");
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
    SecretKey hmacKey = BCJceUtils.generateSecretKey("HmacSHA256");
    assertThat(BCJceUtils.computeMac("HmacSHA256", hmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}", BCJceUtils.computeMac("HmacSHA256", hmacKey, input.getBytes()).length);

    //AESCMAC test
    SecretKey cmacKey = BCJceUtils.generateSecretKey("AES");
    assertThat(BCJceUtils.computeMac("AESCMAC", cmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}", BCJceUtils.computeMac("AESCMAC", cmacKey, input.getBytes()).length);
  }

  @Test
  void getAlgorithmParamGen() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = BCJceUtils.getAlgorithmParamGen("GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = BCJceUtils.generateSecretKey("AES");

    BCSymmetricCrypto bcSymmetricCrypto = new BCSymmetricCrypto("AES/GCM/NoPadding", secretKey, algParamsGCM);
    byte[] encryptedInput0 = bcSymmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = bcSymmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("GCM encryptedInput0 : {}", encryptedInput0);
    log.debug("GCM decryptedInput0 : {}", new String(decryptedInput0));
  }
}