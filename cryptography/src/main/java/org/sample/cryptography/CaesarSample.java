package org.sample.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.sample.cryptography.impl.Algorithm;
import org.sample.cryptography.impl.Cipher;
import org.sample.cryptography.impl.Key;


@Slf4j
public class CaesarSample {

    public static void main(String[] args) {
        byte[] plainText = "Hello World".getBytes();

        Algorithm algorithm = Algorithm.CAESAR;
        Cipher caearCipher = algorithm.getCipher();
        Key key = algorithm.getKey();

        log.info("Using CAESAR cipher ...");
        log.info("PlainText '{}'", new String(plainText));
        log.info("Key '{}'", new String(key.getEncoded()));

        byte[] cipherText = caearCipher.encrypt(plainText, key);
        log.info("Encrypted CipherText '{}'", new String(cipherText));
        log.info("Decrypted PlainText '{}'", new String(caearCipher.decrypt(cipherText, key)));

    }
}
