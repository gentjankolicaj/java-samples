package org.sample.jetty_12.websocket.tls;

import static org.assertj.core.api.Assertions.assertThat;

import io.jdev.jackson.YamlConfigurations;
import jakarta.websocket.CloseReason.CloseCodes;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Future;
import org.awaitility.Awaitility;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.transport.HttpClientTransportDynamic;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sample.jetty_12.JettyBuilder;
import org.sample.jetty_12.JettyProperties;
import org.sample.jetty_12.JettyServer;
import org.sample.jetty_12.websocket.WSTest;

/**
 * @author gentjan kolicaj
 * @Date: 11/28/24 7:05 PM
 */
class TLSHelloEndpointTest extends WSTest {

  JettyServer jettyServer;

  @Test
  void websocketWithTLS() throws Exception {
    JettyProperties jettyProperties = YamlConfigurations.load(JettyProperties.class,
        "/jetty_https_versions.yaml");

    //Use new builder to create servlet context
    ServletContextHandler contextHandler = JettyBuilder.newWebSocketBuilder()
        .securityOption()
        .contextPath("/")
        .jettyApiEndpoint((req, res) -> new TLSHelloEndpoint(), TLSHelloEndpoint.URI)
        .build();

    //jetty server creation
    jettyServer = new JettyServer(jettyProperties.getJettyServer(), contextHandler);
    jettyServer.start();

    String scheme = "wss";
    String host = "127.0.0.1";
    int port = 8444;
    String path = TLSHelloEndpoint.URI;

    /////////////////////////////////////////////////
    //I am using jetty-websocket-client because ease with TLS on websockets
    //create jetty ssl context factory for websocket client.
    var sslContextFactory = new SslContextFactory.Client();
    // Trust all certificates for testing (not recommended for production)
    sslContextFactory.setTrustAll(true);

    //HttpClient setup
    ClientConnector clientConnector = new ClientConnector();
    clientConnector.setSslContextFactory(sslContextFactory);
    HttpClient httpClient = new HttpClient(new HttpClientTransportDynamic(clientConnector));

    //Create web socket client endpoint
    JettyClientEndpoint clientEndpoint = new JettyClientEndpoint();
    WebSocketClient webSocketClient = new WebSocketClient(httpClient);
    webSocketClient.start();

    //Create http/1.1 client websocket session
    Future<Session> session = webSocketClient.connect(clientEndpoint,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !clientEndpoint.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(clientEndpoint.getTextQueue().pollFirst()).contains("Hello");
    session.get()
        .close(CloseCodes.NORMAL_CLOSURE.getCode(), "http/1.1 test finished", Callback.NOOP);

    //Create http/2 client websocket session
    //change port because http2 is on different port & connector
    port = 8445;
    JettyClientEndpoint clientEndpoint2 = new JettyClientEndpoint();
    Future<Session> session2 = webSocketClient.connect(clientEndpoint2,
        URI.create(String.format("%s://%s:%d%s", scheme, host, port, path)));

    //wait for websocket messages to arrive at queue
    Awaitility.await().until(() -> !clientEndpoint2.getTextQueue().isEmpty());

    //check if queue contains string with 2024 date (which it does because of pushing server thread).
    assertThat(clientEndpoint2.getTextQueue().pollFirst()).contains("Hello");
    session2.get()
        .close(CloseCodes.NORMAL_CLOSURE.getCode(), "http/2 test finished", Callback.NOOP);

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