package org.sample.websocket.annotated;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.Set;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24
 * @Time: 11:47 AM
 */
public class EchoEndpointConfiguration implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
    return Set.of();
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
    return Set.of(EchoEndpointA.class);
  }
}
