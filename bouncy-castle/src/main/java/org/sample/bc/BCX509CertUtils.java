package org.sample.bc;

import static org.sample.bc.BCConstants.BC_SECURITY_PROVIDER;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class BCX509CertUtils {

  private static final Map<String, X509CertificateHolder> certMap = new ConcurrentHashMap<>();
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private static void init() {
    try {
      x509CertificateFactory = CertificateFactory.getInstance("X.509", BC_SECURITY_PROVIDER);
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }


  public static X509Certificate getX509Cert(X509CertificateHolder x509CertificateHolder)
      throws GeneralSecurityException, IOException {
    return (X509Certificate) x509CertificateFactory.generateCertificate(
        new ByteArrayInputStream(x509CertificateHolder.getEncoded()));
  }

  public static X500Name toIETFName(X500Name x500Name) {
    return X500Name.getInstance(RFC4519Style.INSTANCE, x500Name);
  }

  public static X500Name createX500Name(String country, String state, String locality,
      String organization, String commonName) {
    return new X500NameBuilder(BCStyle.INSTANCE)
        .addRDN(BCStyle.C, country)
        .addRDN(BCStyle.ST, state)
        .addRDN(BCStyle.L, locality)
        .addRDN(BCStyle.O, organization)
        .addRDN(BCStyle.CN, commonName)
        .build();
  }


}
