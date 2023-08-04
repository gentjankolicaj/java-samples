package org.sample.bc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.AlgorithmParameterGenerator;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

@Slf4j
public class BCJceUtils {


  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";
  public static final String ALGORITHM_CAN_T_BE_EMPTY = "Algorithm can't be empty.";

  private static JcaPEMKeyConverter jcaPEMKeyConverter;
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private BCJceUtils() {
  }

  private static void init() {
    try {
      x509CertificateFactory = CertificateFactory.getInstance("X.509", BC_SECURITY_PROVIDER);
      jcaPEMKeyConverter = new JcaPEMKeyConverter();
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }

  public static void logSetup() {
    try {
      Provider[] providers = Security.getProviders();
      if (ArrayUtils.isNotEmpty(providers)) {
        StringBuilder sb = new StringBuilder();
        for (Provider provider : providers) {
          sb.append(provider.getName()).append(" version:").append(provider.getVersionStr()).append(" info:")
              .append(provider.getInfo()).append("\n");
        }
        log.info("Security providers : \n{}", sb);
      } else {
        log.info("Security providers : 0.");
      }
      log.info("AES max key length : {}", Cipher.getMaxAllowedKeyLength("AES"));
    } catch (Exception e) {
      log.error("Error ", e);
    }
  }


  /**
   * <br>On handling pem file keys : <a href="https://www.baeldung.com/java-read-pem-file-keys">Pem file keys</a>
   * <br>PKCS8EncodedKeySpec is class for handling private keys material.More at <a
   * href="https://en.wikipedia.org/wiki/PKCS_8">PKCS8 format standard</a>
   *
   * @param pemPath Path of PEM file containing private key.
   * @return optional RSA private key
   */
  public static Optional<RSAPrivateKey> loadRSAPrivateKeyPem(Path pemPath) {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemKeyPair.getPrivateKeyInfo());
      return Optional.of((RSAPrivateKey) jcaPEMKeyConverter.getPrivateKey(privateKeyInfo));
    } catch (Exception e) {
      log.error("Error loading PEM private key : ", e);
      return Optional.empty();
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
  public static Optional<RSAPublicKey> loadRSAPublicKeyPem(Path pemPath) {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
      return Optional.of((RSAPublicKey) jcaPEMKeyConverter.getPublicKey(subjectPublicKeyInfo));
    } catch (Exception e) {
      log.error("Error loading PEM public key : ", e);
      return Optional.empty();
    }
  }

  /**
   * Load X509Certificate from cert path
   *
   * @param certPath certification path
   * @return X509 certificate
   * @throws FileNotFoundException when file not found on path
   * @throws CertificateException  when there are certificate issues
   */
  public static X509Certificate loadX509Certificate(Path certPath) throws FileNotFoundException, CertificateException {
    return (X509Certificate) x509CertificateFactory.generateCertificate(new FileInputStream(certPath.toFile()));
  }

  public static IvParameterSpec generateIvParameterSpec(String secureRandomAlgorithm, int vectorLength)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(secureRandomAlgorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
    byte[] vector = new byte[vectorLength];
    secureRandom.nextBytes(vector);
    return new IvParameterSpec(vector);
  }

  public static SecretKey generateSecretKey(String algorithm) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, BC_SECURITY_PROVIDER);
    return keyGenerator.generateKey();
  }

  public static SecretKeySpec createSecretKeySpec(String algorithm, byte[] keyBytes) {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (ArrayUtils.isEmpty(keyBytes)) {
      throw new IllegalArgumentException("Key bytes can't be empty.");
    }
    return new SecretKeySpec(keyBytes, algorithm);
  }

  public static byte[] computeDigest(String algorithm, byte[] input) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }

    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException("Input can't be empty.");
    }
    MessageDigest messageDigest = MessageDigest.getInstance(algorithm, BC_SECURITY_PROVIDER);
    return messageDigest.digest(input);
  }

  public static DigestCalculator createDigestCalculator(String algorithm) throws OperatorException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    DigestAlgorithmIdentifierFinder algorithmIdentifierFinder = new DefaultDigestAlgorithmIdentifierFinder();
    DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider(
        BC_SECURITY_PROVIDER).build();
    return digestCalculatorProvider.get(algorithmIdentifierFinder.find(algorithm));

  }

  /**
   * Return a MAC computed over input using the passed in MAC algorithm name
   *
   * @param algorithm the name of the MAC algorithm.
   * @param key       an appropriate secret key for the MAC algorithm.
   * @param input     the input for the MAC function.
   * @return the computed MAC.
   */
  public static byte[] computeMac(String algorithm, Key key, byte[] input) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException("Key can't be empty.");
    }

    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException("Input can't be empty.");
    }
    Mac mac = Mac.getInstance(algorithm, BC_SECURITY_PROVIDER);
    mac.init(key);
    mac.update(input);
    return mac.doFinal();
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public static AlgorithmParameterGenerator getAlgorithmParamGen(String algorithm) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    return AlgorithmParameterGenerator.getInstance(algorithm, BC_SECURITY_PROVIDER);
  }


}
