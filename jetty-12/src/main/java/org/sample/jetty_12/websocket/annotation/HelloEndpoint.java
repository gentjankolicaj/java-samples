package org.sample.jetty_12.websocket.annotation;

import static org.sample.jetty_12.websocket.annotation.HelloEndpoint.URI;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 10:56 PM
 */
@ServerEndpoint(URI)
@Slf4j
public class HelloEndpoint {

  public static final String URI = "/hello";
  private Session session;

  @OnOpen
  public void onOpen(Session session) {
    log.info("server open session: {}", session);
    this.session = session;
    new Thread(() -> pushHello()).start();
  }

  @OnMessage
  public void onText(String message) {
    log.warn("server received-message : {}", message);
  }

  @OnClose
  public void onClose(CloseReason close) {
    log.info("server close reason {}", close);
    this.session = null;
  }

  private void pushHello() {
    String helloMessage = "Hello";
    while (this.session != null) {
      try {
        this.session.getBasicRemote().sendText(helloMessage);
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }
  }

}