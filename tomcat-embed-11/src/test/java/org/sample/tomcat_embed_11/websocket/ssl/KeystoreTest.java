package org.sample.tomcat_embed_11.websocket.ssl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/20/24 7:01 PM
 */
public class KeystoreTest {


  @Test
  void readKeystoreJKS() throws KeyStoreException {
    char[] keystorePassword = "123456".toCharArray();
    String tomcatKeyAlias = "tomcat-alias";
    KeyStore keyStore = KeyStore.getInstance("JKS");

    //load from file
    URL url = getClass().getResource("/ssl/keystore.jks");
    try (FileInputStream fis = new FileInputStream(url.getFile())) {
      keyStore.load(fis, keystorePassword);
    } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    //public key loaded from keystore
    Certificate cert = keyStore.getCertificate(tomcatKeyAlias);
    assertThat(cert).isNotNull();
    assertThat(cert.getPublicKey()).isNotNull();
  }

  @Test
  void readKeystoreP12() throws KeyStoreException {
    char[] keystorePassword = "1234567".toCharArray();
    String tomcatKeyAlias = "tomcat-alias";
    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    //load from file
    URL url = getClass().getResource("/ssl/keystore.p12");
    try (FileInputStream fis = new FileInputStream(url.getFile())) {
      keyStore.load(fis, keystorePassword);
    } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    //public key loaded from keystore
    Certificate cert = keyStore.getCertificate(tomcatKeyAlias);
    assertThat(cert).isNotNull();
    assertThat(cert.getPublicKey()).isNotNull();
  }

}
