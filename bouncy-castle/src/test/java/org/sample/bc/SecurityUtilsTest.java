package org.sample.bc;

import java.security.Security;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

  static {
    Security.addProvider(new BouncyCastlePQCProvider());
  }

  @Test
  void printProviders() {
    SecurityUtils.logSecurityProviders();
  }
}