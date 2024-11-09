package org.sample.websocket.string;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 11:47 AM
 *
 * This implementation is used when war is deployed in tomcat.Tomcat uses this to find Endpoint classes.
 */
public class StringEndpointConfiguration implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
    Set<ServerEndpointConfig> configs = new HashSet<>();
    if (set.contains(StringEndpoint.class)) {
      configs.add(
          ServerEndpointConfig.Builder.create(StringEndpoint.class, StringEndpoint.ENDPOINT_URI)
              .build());
    }
    return configs;
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
    return Set.of();
  }
}
