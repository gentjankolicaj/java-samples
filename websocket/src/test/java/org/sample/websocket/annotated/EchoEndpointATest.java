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
@SuppressWarnings("unused")
class EchoEndpointATest {

  @Test
  void endpoint() throws LifecycleException, IOException, DeploymentException {
    TomcatServer tomcatServer = new TomcatServer(8080, WSConfig.class);
    tomcatServer.start();

    //websocket client request
    WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
    Session wsSession = wsContainer.connectToServer(CurrentClientEndpoint.class,
        URI.create("ws://localhost:8080/" + EchoEndpointA.ENDPOINT_URI));

    //use session to send string message to server.
    wsSession.getBasicRemote().sendText("'client message: annotated-string'");

    //stop tomcat after 6 seconds
    Awaitility.await()
        .timeout(Duration.ofSeconds(6))
        .pollDelay(Duration.ofSeconds(5))
        .untilAsserted(tomcatServer::stop);

    //blocking join until close is called.
    tomcatServer.join();

  }


  public static class WSConfig extends AbstractWSConfig {

    public WSConfig() {
      super(Set.of(EchoEndpointA.class), Set.of());
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