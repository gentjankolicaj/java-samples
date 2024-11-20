package org.sample.tomcat_embed_11.websocket.ssl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/20/24 6:52 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSLProperties {

  protected String keystoreFile;
  protected String keystorePassword;
  protected String certKeyPassword;
  protected String certAlias;

}
