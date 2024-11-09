package org.sample.websocket;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/8/24 5:23 PM
 */
@Slf4j
class AbstractWSConfigTest {

  @Test
  void config() throws LifecycleException, IOException, DeploymentException {
    TomcatServer tomcatServer = new TomcatServer(8080, TestAbstractWSConfig.class);
    tomcatServer.start();

    //websocket client request
    WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
    Session wsSession = wsContainer.connectToServer(TestClientEndpoint.class,
        URI.create("ws://localhost:8080/test-endpoint"));

    wsSession.getBasicRemote().sendText("Client test message.");

    //stop tomcat after 6 seconds
    Awaitility.await()
        .timeout(Duration.ofSeconds(15))
        .pollDelay(Duration.ofSeconds(14))
        .untilAsserted(tomcatServer::stop);

    //blocking join until close is called.
    tomcatServer.join();
  }


  @SuppressWarnings("unused")
  @Slf4j
  @ServerEndpoint("/test-endpoint")
  @Getter
  public static class TestServerEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("server: session opened.");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
      log.info("server received : {}", message);
      session.getBasicRemote().sendText("return-back-" + message);
    }
  }

  @SuppressWarnings("unused")
  @Slf4j
  @ClientEndpoint
  @Getter
  public static class TestClientEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("client: Session opened.");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
      log.info("client received : {}", message);
    }
  }


  public static class TestAbstractWSConfig extends AbstractWSConfig {

    public TestAbstractWSConfig() {
      super(Set.of(TestServerEndpoint.class), Set.of());
    }

  }


}