package org.sample.websocket;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.junit.jupiter.api.Test;

/**
 * @author gentjan kolicaj
 * @Date: 11/8/24 5:23 PM
 */
@Slf4j
class WebSocketEndpointsTest {

  @Test
  void websocket() throws LifecycleException {
    TomcatServer tomcatServer = new TomcatServer(8080, new TestWSContext());
    tomcatServer.start();

    //blocking join until close is called.
    tomcatServer.join();
  }

  @ServerEndpoint("/ws")
  @Getter
  public static class TestEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
      this.session = session;
      log.info("Session opened.");
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
      log.info("Received-message: {}", message);
      session.getBasicRemote().sendText("Return-back: " + message);
    }
  }

  public static class TestWSContext extends WebSocketEndpoints {

    public TestWSContext() {
      super(Set.of(TestEndpoint.class));
    }
  }

}