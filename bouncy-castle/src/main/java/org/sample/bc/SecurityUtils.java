package org.sample.bc;

import java.security.Provider;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public class SecurityUtils {


  public static void logSecurityProviders() {
    Provider[] providers = Security.getProviders();
    if (ArrayUtils.isNotEmpty(providers)) {
      StringBuilder sb = new StringBuilder();
      for (Provider provider : providers) {
        sb.append(provider.getName()).append(" version:").append(provider.getVersionStr()).append(" info:")
            .append(provider.getInfo()).append("\n");
      }
      log.info("Security providers : \n {}", sb);
    } else {
      log.info("Security providers 0.");
    }

  }

}
