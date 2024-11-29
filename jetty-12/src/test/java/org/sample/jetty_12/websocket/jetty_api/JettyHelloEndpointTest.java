package org.sample.jetty_12.websocket.jetty_api;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.websocket.CloseReason.CloseCodes;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Future;
import org.awaitility.Awaitility;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.websocket.WSTest;

/**
 * @author gentjan kolicaj
 * @Date: 11/28/24 7:05 PM
 */
class JettyHelloEndpointTest extends WSTest {

  JettyServer jettyServer;

  @Test
  void websocketWithJettyApi() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_http_versions.yaml");

    // This is also known as the handler tree (in jetty speak)
    ServletContextHandler contextHandler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    contextHandler.setContextPath("/");

    // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
    contextHandler.addServlet(DefaultServlet.class, "/");

    // Add WebSocket server endpoints
    //Here we use specific jetty websocket api
    JettyWebSocketServletContainerInitializer.configure(contextHandler, (context, configurator) -> {
      configurator.addMapping(JettyHelloEndpoint.WEBSOCKET_URI,
          JettyHelloEndpoint.getWebSocketCreator());
    });

    //jetty server creation
    jettyServer = new JettyServer(jettyProperties.getJettyServer(), contextHandler);
    jettyServer.start();

    String scheme = "ws";
    String host = "localhost";
    int port = 8081;
    String path = JettyHelloEndpoint.WEBSOCKET_URI;

    //Create jetty websocket client endpoint
    WebSocketClient webSocketClient = new WebSocketClient();
    webSocketClient.start();

    //Jetty websocket endpoint
    JettyClientEndpoint jettyClientEndpoint = new JettyClientEndpoint();

    //Create http/1.1 client websocket session
    Future<Session> future = webSocketClient.connect(jettyClientEndpoint,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !jettyClientEndpoint.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(jettyClientEndpoint.getTextQueue().pollFirst()).contains("Hello");
    future.get()
        .close(CloseCodes.NORMAL_CLOSURE.getCode(), "http/1.1 test finished", Callback.NOOP);

    //Create http/2 client websocket session
    //change port because http2 is on different port & connector
    port = 8082;
    JettyClientEndpoint jettyClientEndpoint2 = new JettyClientEndpoint();
    Future<Session> future2 = webSocketClient.connect(jettyClientEndpoint2,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !jettyClientEndpoint2.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(jettyClientEndpoint2.getTextQueue().pollFirst()).contains("Hello");
    future2.get().close(CloseCodes.NORMAL_CLOSURE.getCode(), "http/2 test finished", Callback.NOOP);

    //async for stopping server
    Awaitility.await()
        .timeout(Duration.ofSeconds(2))
        .pollDelay(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          jettyServer.stop();
          webSocketClient.stop();
        });

    //blocking join until close is called.
    jettyServer.join();
  }

  @AfterEach
  void testClean() throws Exception {
    jettyServer.stop();
  }


}