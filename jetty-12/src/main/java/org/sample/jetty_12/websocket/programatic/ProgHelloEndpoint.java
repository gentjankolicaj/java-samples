package org.sample.jetty_12.websocket.programatic;


import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler.Whole;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 10:56 PM
 */
@ServerEndpoint(ProgHelloEndpoint.WEBSOCKET_URI)
@Slf4j
public class ProgHelloEndpoint extends Endpoint {

  protected static final String WEBSOCKET_URI = "/prog_hello";
  private Session session;


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    this.session = session;
    this.setup(session);
  }

  @Override
  public void onClose(Session session, CloseReason closeReason) {
    log.info("server close reason {}", closeReason.getReasonPhrase());
    this.session = null;
  }

  public void setup(Session session) {
    log.info("server open session: {}", session);
    new Thread(() -> pushHello()).start();
    session.addMessageHandler(new TextMessageHandler());
  }

  private void pushHello() {
    String helloMessage = "Hello";
    while (this.session != null) {
      try {
        this.session.getBasicRemote().sendText(helloMessage);
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  static class TextMessageHandler implements Whole<String> {

    @Override
    public void onMessage(String message) {
      log.warn("server received-message : {}", message);
    }

  }


}