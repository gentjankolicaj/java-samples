package org.sample.bc;

import java.security.KeyPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.X509CertificateHolder;

@RequiredArgsConstructor
@Getter
public class BCX509CertKeyPair {

  private final KeyPair keyPair;
  private final X509CertificateHolder cert;

}
