package org.sample.websocket.annotated;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.sample.websocket.AbstractWSConfig;
import org.sample.websocket.TomcatServer;

/**
 * @author gentjan kolicaj
 * @Date: 11/9/24 4:20 PM
 */
class BothEndpointsTest {

  @Test
  void bothEndpoints() throws LifecycleException, IOException, DeploymentException {
    TomcatServer tomcatServer = new TomcatServer(8080, WSConfig.class);
    tomcatServer.start();

    //websocket client request
    WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();

    //use session to send string message to server.
    Session wsSession = wsContainer.connectToServer(CurrentClientEndpoint.class,
        URI.create("ws://localhost:8080/" + EchoEndpointA.ENDPOINT_URI));
    wsSession.getBasicRemote().sendText("'client message: annotated-string'");

    //use session 2 to send byte buffer to server.
    Session wsSession2 = wsContainer.connectToServer(CurrentClientEndpoint.class,
        URI.create("ws://localhost:8080/" + EchoEndpointB.ENDPOINT_URI));
    ByteBuffer buffer = ByteBuffer.wrap("123345678".getBytes());
    wsSession2.getBasicRemote().sendBinary(buffer);

    //stop tomcat after 11 seconds
    Awaitility.await()
        .timeout(Duration.ofSeconds(6))
        .pollDelay(Duration.ofSeconds(5))
        .untilAsserted(tomcatServer::stop);

    //blocking join until close is called.
    tomcatServer.join();

  }


  public static class WSConfig extends AbstractWSConfig {

    public WSConfig() {
      super(Set.of(EchoEndpointA.class, EchoEndpointB.class), Set.of());
    }
  }


  @Slf4j
  @ClientEndpoint
  @Getter
  public static class CurrentClientEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("client: session opened.");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
      log.info("client received string : {}", message);
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer buffer) {
      log.info("client received buffer : {}", buffer.array());
    }
  }


}