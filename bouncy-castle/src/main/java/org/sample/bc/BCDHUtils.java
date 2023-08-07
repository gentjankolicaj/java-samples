package org.sample.bc;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.jcajce.spec.DHUParameterSpec;
import org.bouncycastle.jcajce.spec.MQVParameterSpec;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.bouncycastle.util.Arrays;

public class BCDHUtils {

  /**
   * Bouncy castle security provider
   */
  static final String BC_SECURITY_PROVIDER = "BC";
  static final String ALGORITHM = "DH";


  private BCDHUtils() {
  }

  /**
   * Generate set of DH parameters suitable for creating keys.
   *
   * @return DHParameterSpec holding the generated parameters.
   */
  public static DHParameterSpec generateParameterSpec(int keySize) throws GeneralSecurityException {
    AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance(ALGORITHM, BC_SECURITY_PROVIDER);
    paramGen.init(keySize);
    AlgorithmParameters params = paramGen.generateParameters();
    return params.getParameterSpec(DHParameterSpec.class);
  }

  /**
   * Generate a 2048 bit DH key pair using provider based parameters.
   *
   * @return a DH KeyPair
   */
  public static KeyPair generateKeyPair(int keySize) throws GeneralSecurityException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, BC_SECURITY_PROVIDER);
    keyPairGenerator.initialize(keySize);
    return keyPairGenerator.generateKeyPair();
  }

  /**
   * Generate a DH key pair using our own specified parameters.
   *
   * @param dhParameterSpec the DH parameters to use for key generation.
   * @return a DH KeyPair
   */
  public static KeyPair generateKeyPair(DHParameterSpec dhParameterSpec) throws GeneralSecurityException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, BC_SECURITY_PROVIDER);
    keyPairGenerator.initialize(dhParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }


  /**
   * Generate an agreed secret byte value of 32 bytes in length.
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey            Party B's public key.
   * @return the first 32 bytes of the generated secret.
   */
  public static byte[] generateSecret(String keyAgreementAlgorithm, PrivateKey aPrivateKey, PublicKey bPublicKey)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BC_SECURITY_PROVIDER);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    return Arrays.copyOfRange(agreement.generateSecret(), 0, 32);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @return the generated secret key .
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BC_SECURITY_PROVIDER);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }


  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @param keyMaterial           key material
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, byte[] keyMaterial) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BC_SECURITY_PROVIDER);
    agreement.init(aPrivateKey, new UserKeyingMaterialSpec(keyMaterial));
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }


  /**
   * Generate an agreed secret key value using the Unified Diffie-Hellman model.
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey            Party B's public key.
   * @param dhuParameterSpec      Unified Diffie-Hellman parameter spec
   * @return the generated secret key.
   */
  public static SecretKey dhuGenerateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, DHUParameterSpec dhuParameterSpec) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BC_SECURITY_PROVIDER);
    agreement.init(aPrivateKey, dhuParameterSpec);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }


  /**
   * Generate an agreed secret key value using MQV.
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey            Party B's public key.
   * @param mqvParameterSpec      MQV parameter spec
   * @return the generated secret key.
   */
  public static SecretKey mqvGenerateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, MQVParameterSpec mqvParameterSpec) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BC_SECURITY_PROVIDER);
    agreement.init(aPrivateKey, mqvParameterSpec);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

}
