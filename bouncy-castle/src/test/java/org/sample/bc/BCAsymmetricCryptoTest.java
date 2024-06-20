package org.sample.bc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BCAsymmetricCryptoTest {


  @Test
  void invalidArguments() throws GeneralSecurityException {
    KeyPair keyPair = BCUtils.generateKeyPair("RSA", 2048);

    //negative tests
    assertThatThrownBy(() -> new BCAsymmetricCrypto("RSA", null)).isInstanceOf(
        NullPointerException.class);
    assertThatThrownBy(
        () -> new BCAsymmetricCrypto("RSA", null, keyPair.getPrivate())).isInstanceOf(
        KeyException.class);

  }

  @Test
  void cipherWithKeys() throws GeneralSecurityException {
    KeyPair keyPair = BCUtils.generateKeyPair("RSA", 2048);

    String input = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";

    //positive test , encrypt with public-key & decrypt with private-key
    BCAsymmetricCrypto bcAsymmetricCrypto0 = new BCAsymmetricCrypto("RSA", keyPair.getPublic(),
        keyPair.getPrivate());
    byte[] encrypted0 = bcAsymmetricCrypto0.encrypt(input.getBytes());
    byte[] decrypted0 = bcAsymmetricCrypto0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input.getBytes());
    log.debug("encrypted0 : {}", new String(encrypted0));
    log.debug("decrypted0 : {}", new String(decrypted0));

    //positive test, encrypt with private-key & decrypt with public-key
    BCAsymmetricCrypto bcAsymmetricCrypto1 = new BCAsymmetricCrypto("RSA", keyPair.getPrivate(),
        keyPair.getPublic());
    byte[] encrypted1 = bcAsymmetricCrypto1.encrypt(input.getBytes());
    byte[] decrypted1 = bcAsymmetricCrypto1.decrypt(encrypted1);
    assertThat(decrypted1).containsExactly(input.getBytes());
    log.debug("encrypted1 : {}", new String(encrypted1));
    log.debug("decrypted1 : {}", new String(decrypted1));

  }

  @Test
  void cipherWithKeyPair() throws GeneralSecurityException {
    KeyPair keyPair = BCUtils.generateKeyPair("RSA", 2048);

    String input = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";

    //positive test with key-pair, encrypt with private-key & decrypt with public-key
    BCAsymmetricCrypto bcAsymmetricCrypto2 = new BCAsymmetricCrypto("RSA", keyPair);
    byte[] encrypted2 = bcAsymmetricCrypto2.encrypt(input.getBytes());
    byte[] decrypted2 = bcAsymmetricCrypto2.decrypt(encrypted2);
    assertThat(decrypted2).containsExactly(input.getBytes());
    log.debug("encrypted2 : {}", new String(encrypted2));
    log.debug("decrypted2 : {}", new String(decrypted2));
  }


}