package org.sample.jetty_12.websocket.tls;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


/**
 * @author gentjan kolicaj
 * @Date: 11/27/24 10:56 PM
 */
@WebSocket
@Slf4j
public class TLSHelloEndpoint {

  public static final String URI = "/tls_hello";
  private Session session;

  @OnWebSocketOpen
  public void onOpen(Session session) {
    log.info("server open session: {}", session);
    this.session = session;
    new Thread(() -> pushHello()).start();
  }

  @OnWebSocketMessage
  public void onText(String message) {
    log.warn("server received-message : {}", message);
  }

  @OnWebSocketClose
  public void onClose(int code, String closeReason) {
    log.info("server close reason {}", closeReason);
    this.session = null;
  }

  private void pushHello() {
    String helloMessage = "Hello";
    while (this.session != null) {
      try {
        this.session.sendText(helloMessage, Callback.NOOP);
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}