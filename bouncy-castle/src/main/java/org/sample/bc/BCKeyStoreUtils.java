package org.sample.bc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.security.auth.x500.X500PrivateCredential;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BCKeyStoreUtils {

  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";

  public static PrivateKey getPrivateKey(Path keystorePath, KeyStoreType keyStoreType,
      String keystorePassword,
      String alias) throws GeneralSecurityException, IOException {
    KeyStore keystore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keystore.load(new FileInputStream(keystorePath.toFile()), keystorePassword.toCharArray());
    return (PrivateKey) keystore.getKey(alias, keystorePassword.toCharArray());
  }


  public static PublicKey getPublicKey(Path keystorePath, KeyStoreType keyStoreType,
      String keystorePassword,
      String alias) throws GeneralSecurityException, IOException {
    KeyStore keystore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keystore.load(new FileInputStream(keystorePath.toFile()), keystorePassword.toCharArray());
    Certificate certificate = keystore.getCertificate(alias);
    return certificate.getPublicKey();
  }

  public static KeyPair getKeyPair(Path keystorePath, KeyStoreType keyStoreType,
      String keystorePassword,
      String alias) throws GeneralSecurityException, IOException {
    KeyStore keystore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keystore.load(new FileInputStream(keystorePath.toFile()), keystorePassword.toCharArray());
    Key key = keystore.getKey(alias, keystorePassword.toCharArray());
    if (key instanceof PrivateKey) {
      Certificate cert = keystore.getCertificate(alias);
      return new KeyPair(cert.getPublicKey(), (PrivateKey) key);
    }
    throw new GeneralSecurityException("Private key not found.");
  }


  public static Certificate getCertificate(Path keystorePath, KeyStoreType keyStoreType,
      String password,
      String certAlias) throws GeneralSecurityException, IOException {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keyStore.load(new FileInputStream(keystorePath.toFile()), password.toCharArray());
    return keyStore.getCertificate(certAlias);
  }

  public static KeyStore loadKeyStore(Path keystorePath, KeyStoreType keyStoreType,
      String keystorePassword)
      throws GeneralSecurityException, IOException {
    KeyStore keystore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keystore.load(new FileInputStream(keystorePath.toFile()), keystorePassword.toCharArray());
    return keystore;
  }

  public static void storeKeyStore(Path newKeystorePath, KeyStoreType keyStoreType,
      String keystorePassword,
      String keyPassword, X500PrivateCredential x500PrivateCredential)
      throws GeneralSecurityException, IOException {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType.getName(), BC_SECURITY_PROVIDER);
    keyStore.load(null, null);
    //set entries of keystore
    keyStore.setKeyEntry(x500PrivateCredential.getAlias(), x500PrivateCredential.getPrivateKey(),
        keyPassword.toCharArray(), new Certificate[]{x500PrivateCredential.getCertificate()});
    //create file in fs
    try (FileOutputStream fos = new FileOutputStream(newKeystorePath.toFile())) {
      keyStore.store(fos, keystorePassword.toCharArray());
    }
  }


}
