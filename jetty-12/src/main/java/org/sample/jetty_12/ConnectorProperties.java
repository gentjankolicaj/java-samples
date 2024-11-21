package org.sample.jetty_12;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 5:42 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnectorProperties {

  protected static final String SECURE_SCHEME_KEY = "";
  protected static final String SECURE_PORT_KEY = "";
  protected String name;
  protected String host;
  protected int port;
  protected TimeoutProperties idleTimeout;
  protected Optional<HttpConfigProperties> httpConfig = Optional.empty();

}
