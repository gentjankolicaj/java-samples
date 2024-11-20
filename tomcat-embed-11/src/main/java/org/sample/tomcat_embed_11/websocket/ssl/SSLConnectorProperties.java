package org.sample.tomcat_embed_11.websocket.ssl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sample.tomcat_embed_11.websocket.ConnectorProperties;

/**
 * @author gentjan kolicaj
 * @Date: 11/13/24 7:37 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSLConnectorProperties extends ConnectorProperties {

  protected SSLProperties sslProperties;

}