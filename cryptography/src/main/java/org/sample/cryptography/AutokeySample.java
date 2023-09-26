package org.sample.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.sample.cryptography.impl.Algorithm;
import org.sample.cryptography.impl.Cipher;
import org.sample.cryptography.impl.Key;
import org.sample.cryptography.impl.TabulaRecta;


@Slf4j
public class AutokeySample {

  public static void main(String[] args) {
    byte[] plainText = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();

    Algorithm algorithm = Algorithm.AUTOKEY;
    Cipher cipher = algorithm.getCipher();
    Key key = algorithm.getKey();

    log.error("{}",TabulaRecta.getCrossCharacterDec(25,25));

    log.info("Using AUTOKEY cipher ...");
    log.info("PlainText '{}'", new String(plainText));
    log.info("Key '{}'", new String(key.getEncoded()));

    byte[] cipherText = cipher.encrypt(plainText, key);
    log.info("Encrypted CipherText '{}'", new String(cipherText));
    log.info("Decrypted PlainText '{}'", new String(cipher.decrypt(cipherText, key)));

    //Just in case assertion
    Assertions.assertThat(cipher.decrypt(cipherText, key)).containsExactly(plainText);

  }

}
