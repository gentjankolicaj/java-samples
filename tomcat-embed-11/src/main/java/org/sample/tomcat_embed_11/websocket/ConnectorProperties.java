package org.sample.tomcat_embed_11.websocket;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/13/24 7:37 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "connector")
public class ConnectorProperties {

  protected String scheme;
  protected boolean secure;
  protected int port;
  protected long asyncTimeout;
  protected boolean allowBackslash;
  protected boolean allowTrace;
  protected String proxyName;
  protected int proxyPort;
  protected int maxSavePostSize;
  protected int maxCookieCount;
  protected int maxParameterCount;
  protected int maxPostSize;
  protected Map<String, String> properties;
}