package org.sample.bc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.sample.bc.exception.CryptException;

@Slf4j
public class BCUtils {


  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";

  private static final JcaPEMKeyConverter JCA_PEM_KEY_CONVERTER;
  private static final CertificateFactory X509_CERTIFICATE_FACTORY;

  static {
    Security.addProvider(new BouncyCastleProvider());
    try {
      X509_CERTIFICATE_FACTORY = CertificateFactory.getInstance("X.509", BC_SECURITY_PROVIDER);
      JCA_PEM_KEY_CONVERTER = new JcaPEMKeyConverter();
      log.info("AES allowed max key length {}", Cipher.getMaxAllowedKeyLength("AES"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  private BCUtils() {
  }


  /**
   * <br>On handling pem file keys : <a href="https://www.baeldung.com/java-read-pem-file-keys">Pem file keys</a>
   * <br>PKCS8EncodedKeySpec is class for handling private keys material.More at <a
   * href="https://en.wikipedia.org/wiki/PKCS_8">PKCS8 format standard</a>
   *
   * @param pemPath Path of pem file containing private key unencrypted.
   * @return RSA private key
   */
  public static RSAPrivateKey getPrivateKeyFromPem(Path pemPath) throws CryptException {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
      return (RSAPrivateKey) JCA_PEM_KEY_CONVERTER.getPrivateKey(privateKeyInfo);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }

  /**
   * <br>On handling pem file keys : <a href="https://www.baeldung.com/java-read-pem-file-keys">Pem file keys</a>
   * <br>X509EncodedKeySpec is class for handling public keys material.More at <a
   * href="https://en.wikipedia.org/wiki/X.509">X509 format standard</a>
   *
   * @param pemPath Path of pem file containing public key
   * @return RSA public key
   */
  public static RSAPublicKey getPublicKeyFromPem(Path pemPath) throws CryptException {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
      return (RSAPublicKey) JCA_PEM_KEY_CONVERTER.getPublicKey(subjectPublicKeyInfo);
    } catch (Exception e) {
      throw new CryptException(e);
    }
  }


  public static X509Certificate generateX509Certificate(Path certPath) throws RuntimeException {
    try {
      return (X509Certificate) X509_CERTIFICATE_FACTORY.generateCertificate(new FileInputStream(certPath.toFile()));
    } catch (CertificateException | FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static IvParameterSpec generateIvParameterSpec(String secureRandomAlgorithm, int vectorLength)
      throws RuntimeException {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
      byte[] vector = new byte[vectorLength];
      secureRandom.nextBytes(vector);
      return new IvParameterSpec(vector);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static SecretKey generateSecretKey(String algorithm) {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, BC_SECURITY_PROVIDER);
      return keyGenerator.generateKey();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static SecretKeySpec createSecretKeySpec(String algorithm, byte[] keyBytes) {
    return new SecretKeySpec(keyBytes, algorithm);
  }

  public static byte[] computeDigest(String algorithm, byte[] input) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(algorithm, BC_SECURITY_PROVIDER);
      return messageDigest.digest(input);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static DigestCalculator createDigestCalculator(String algorithm) {
    try {
      DigestAlgorithmIdentifierFinder algorithmIdentifierFinder = new DefaultDigestAlgorithmIdentifierFinder();
      DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider(
          BC_SECURITY_PROVIDER).build();
      return digestCalculatorProvider.get(algorithmIdentifierFinder.find(algorithm));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return a MAC computed over input using the passed in MAC algorithm name
   *
   * @param algorithm the name of the MAC algorithm.
   * @param key       an appropriate secret key for the MAC algorithm.
   * @param input     the input for the MAC function.
   * @return the computed MAC.
   */
  public static byte[] computeMac(String algorithm, Key key, byte[] input) {
    try {
      Mac mac = Mac.getInstance(algorithm, BC_SECURITY_PROVIDER);
      mac.init(key);
      mac.update(input);
      return mac.doFinal();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
