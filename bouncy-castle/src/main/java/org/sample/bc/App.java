package org.sample.bc;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  public static void main(String[] args) throws GeneralSecurityException {
    String input = "Hello world !!!`1234567890-=+_)(*&^%$#@!~{}|?><S:";
    log.info("Input : {}", input);

    //Symmetric key
    SecretKey secretKey = BCUtils.generateSecretKey("AES");
    IvParameterSpec ivParameterSpec = BCUtils.generateIvParameterSpec("SHA1PRNG", 16);
    BCSymmetricCrypto bcSymmetricCrypto = new BCSymmetricCrypto("AES/CBC/CTSPadding", secretKey,
        ivParameterSpec);
    byte[] symmetricKeyEncrypted = bcSymmetricCrypto.encrypt(input.getBytes());
    byte[] symmetricKeyDecrypted = bcSymmetricCrypto.decrypt(symmetricKeyEncrypted);
    log.info("AES/CBC/CTSPadding encrypted : {}", new String(symmetricKeyEncrypted));
    log.info("AES/CBC/CTSPadding decrypted : {}", new String(symmetricKeyDecrypted));

    //Asymmetric key
    KeyPair keyPair = BCUtils.generateKeyPair("RSA", 4096);
    BCAsymmetricCrypto bcAsymmetricCrypto = new BCAsymmetricCrypto("RSA", keyPair);
    byte[] asymmetricKeyEncrypted = bcAsymmetricCrypto.encrypt(input.getBytes());
    byte[] asymmetricKeyDecrypted = bcAsymmetricCrypto.decrypt(asymmetricKeyEncrypted);
    log.info("RSA encrypted : {}", new String(asymmetricKeyEncrypted));
    log.info("RSA decrypted : {}", new String(asymmetricKeyDecrypted));
  }

}
