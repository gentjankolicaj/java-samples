package org.sample.tomcat_embed_11.websocket;

import jakarta.servlet.ServletContextEvent;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerContainer;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.server.Constants;
import org.apache.tomcat.websocket.server.WsContextListener;


/**
 * @author gentjan kolicaj
 * @Date: 11/8/24 5:10 PM
 */
@Slf4j
@Getter
public class TomcatWebSocketConfig extends WsContextListener {

  protected Set<Class<?>> annotatedEndpoints;
  protected Set<ProgrammaticEndpoint> programmaticEndpoints;

  public TomcatWebSocketConfig() {
    this.annotatedEndpoints = Set.of();
    this.programmaticEndpoints = Set.of();
  }

  public TomcatWebSocketConfig(Set<Class<?>> annotatedEndpoints,
      Set<ProgrammaticEndpoint> programmaticEndpoints) {
    this.annotatedEndpoints = annotatedEndpoints;
    this.programmaticEndpoints = programmaticEndpoints;
  }


  @Override
  public void contextInitialized(ServletContextEvent sce) {
    super.contextInitialized(sce);

    ServerContainer sc = (ServerContainer) sce.getServletContext()
        .getAttribute(Constants.SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE);

    if (annotatedEndpoints != null && !annotatedEndpoints.isEmpty()) {
      annotatedEndpoints.forEach(endpoint -> {
        try {
          sc.addEndpoint(endpoint);
        } catch (DeploymentException de) {
          log.error("Annotated endpoint deployment error: ", de);
        }
      });
    }

    if (programmaticEndpoints != null && !programmaticEndpoints.isEmpty()) {
      programmaticEndpoints.forEach(endpoint -> {
        try {
          sc.addEndpoint(
              ServerEndpointConfig.Builder.create(endpoint.endpointClass(),
                  endpoint.path()).build());
        } catch (DeploymentException de) {
          log.error("ProgrammaticEndpoint deployment error: ", de);
        }
      });
    }
  }

}
