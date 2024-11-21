package org.sample.jetty_12;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gentjan kolicaj
 * @Date: 11/21/24 8:49 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpConfigProperties extends HashMap<String, Object> {

  protected static final String SECURE_SCHEME_KEY = "secureScheme";
  protected static final String SECURE_PORT_KEY = "securePort";
  private static final String RESPONSE_HEADER_SIZE = "responseHeaderSize";
  private static final String REQUEST_HEADER_SIZE = "requestHeaderSize";
  private static final String OUTPUT_BUFFER_SIZE = "outputBufferSize";

  protected SSLProperties ssl;

  public String getSecureScheme() {
    return (String) get(SECURE_SCHEME_KEY);
  }

  public Integer getSecurePort() {
    return (Integer) get(SECURE_PORT_KEY);
  }

  public int getResponseHeaderSize() {
    return (Integer) get(RESPONSE_HEADER_SIZE);
  }

  public int getRequestHeaderSize() {
    return (Integer) get(REQUEST_HEADER_SIZE);
  }

  public int getOutputBufferSize() {
    return (Integer) get(OUTPUT_BUFFER_SIZE);
  }
}
