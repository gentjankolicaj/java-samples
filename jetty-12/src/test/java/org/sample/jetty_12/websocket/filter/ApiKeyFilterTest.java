package org.sample.jetty_12.websocket.filter;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.servlet.DispatcherType;
import jakarta.websocket.CloseReason;
import jakarta.websocket.CloseReason.CloseCodes;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;
import java.time.Duration;
import java.util.EnumSet;
import org.awaitility.Awaitility;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.websocket.WSTest;


/**
 * @author gentjan kolicaj
 * @Date: 11/29/24 1:37 PM
 */
class ApiKeyFilterTest extends WSTest {

  JettyServer jettyServer;

  @Test
  void defaultServletWithWS() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_http_versions.yaml");

    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
    context.addServlet(DefaultServlet.class, "/");

    //Add filter for websocket endpoint uri
    EnumSet<DispatcherType> dispatchers = EnumSet.allOf(DispatcherType.class);
    context.addFilter(ApiKeyFilter.class, HelloEndpoint.WEBSOCKET_URI, dispatchers);

    // Add WebSocket server endpoints
    JakartaWebSocketServletContainerInitializer.configure(context,
        (servletContext, wsContainer) -> wsContainer.addEndpoint(HelloEndpoint.class));

    //jetty server creation
    jettyServer = new JettyServer(jettyProperties.getJettyServer(), context);
    jettyServer.start();

    String scheme = "ws";
    String host = "localhost";
    int port = 8081;
    String path = HelloEndpoint.WEBSOCKET_URI;

    //Create web socket client endpoint & web socket container
    WSClientEndpoint clientEndpoint = new WSClientEndpoint();
    WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

    //Create http/1.1 client websocket session
    Session session = webSocketContainer.connectToServer(clientEndpoint,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !clientEndpoint.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(clientEndpoint.getTextQueue().pollFirst()).contains("Hello");
    session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "http/1.1 test finished"));

    //Create http/2 client websocket session
    //change port because http2 is on different port & connector
    port = 8082;
    Session session2 = webSocketContainer.connectToServer(clientEndpoint,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !clientEndpoint.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(clientEndpoint.getTextQueue().pollFirst()).contains("Hello");
    session2.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "http/2 test finished"));

    //async for stopping server
    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @AfterEach
  void testClean() throws Exception {
    jettyServer.stop();
  }


}