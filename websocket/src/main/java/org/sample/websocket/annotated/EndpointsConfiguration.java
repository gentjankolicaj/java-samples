package org.sample.websocket.annotated;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.Set;

/**
 * @author gentjan kolicaj
 * @Date: 11/7/24 11:47 AM
 *
 * This implementation of interface is used when war is deployed in tomcat.Tomcat uses this to find Endpoint classes.
 */
public class EndpointsConfiguration implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
    return Set.of();
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
    return Set.of(EchoEndpointA.class, EchoEndpointB.class);
  }
}
