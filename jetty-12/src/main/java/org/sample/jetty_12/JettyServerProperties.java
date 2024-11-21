package org.sample.jetty_12;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/15/24 10:17 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JettyServerProperties {

  protected boolean dumpAfterStart;
  protected boolean dumpBeforeStop;
  protected boolean stopAtShutdown;
  protected long stopTimeout;
  protected boolean gzipEnabled;
  protected ThreadPoolProperties threadPool;
  protected List<ConnectorProperties> connectors;

}
