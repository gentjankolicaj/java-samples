package org.sample.bc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

@Slf4j
class BCUtilsTest {

  @Test
  void getPrivateKeyFromPem() {
  }

  @Test
  void getPublicKeyFromPem() {
  }

  @Test
  void generateX509Certificate() {
  }

  @Test
  void generateIvParameterSpec() {
    assertThat(BCUtils.generateIvParameterSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void generateSecretKey() {
    assertThat(BCUtils.generateSecretKey("HmacSHA256")).isNotNull();
    assertThat(BCUtils.generateSecretKey("AES")).isNotNull();
  }

  @Test
  void createSecretKeySpec() {
    byte[] keyBytes = Hex.decode("2ccd85dfc8d18cb5d84fef4b198554679fece6e8692c9147b0da983f5b7bd417");
    assertThat(BCUtils.createSecretKeySpec("HmacSHA256", keyBytes)).isNotNull();
  }

  @Test
  void computeDigest() {
    String input = "Hello world223fqwerwqrwqrw{}{qre|'/.,~wq~!@#$(*)-=-+_";
    assertThat(BCUtils.computeDigest("SHA-256", input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("SHA-256 size {} : {} ", BCUtils.computeDigest("SHA-256", input.getBytes()).length,
        BCUtils.computeDigest("SHA-256", input.getBytes()));
  }

  @Test
  void createDigestCalculator() throws IOException {
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
  void computeMac() {
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
}