package org.sample.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.sample.cryptography.cipher.Algorithm;
import org.sample.cryptography.cipher.Cipher;
import org.sample.cryptography.cipher.Key;


@Slf4j
public class TrithemiusSample {

  public static void main(String[] args) {
    byte[] plainText = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();

    Algorithm algorithm = Algorithm.VIGENERE;
    Cipher cipher = algorithm.getCipher();
    Key key = algorithm.getKey();

    log.info("Using Trithemius cipher ...");
    log.info("PlainText '{}'", new String(plainText));
    log.info("Key '{}'", new String(key.getEncoded()));

    byte[] cipherText = cipher.encrypt(plainText, key);
    log.info("Encrypted CipherText '{}'", new String(cipherText));
    log.info("Decrypted PlainText '{}'", new String(cipher.decrypt(cipherText, key)));

    //Just in case assertion
    Assertions.assertThat(cipher.decrypt(cipherText, key)).containsExactly(plainText);
  }

}
