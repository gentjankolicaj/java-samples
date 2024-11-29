package org.sample.jetty_12.websocket.jetty_api;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketCreator;
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
@Slf4j
@WebSocket
public class JettyHelloEndpoint {

  public static final String WEBSOCKET_URI = "/hello";
  private Session session;

  public static JettyWebSocketCreator getWebSocketCreator() {
    return (jettyServerUpgradeRequest, jettyServerUpgradeResponse) -> new JettyHelloEndpoint();
  }

  @OnWebSocketOpen
  public void onOpen(Session session) {
    log.info("jetty-server open session: {}", session);
    this.session = session;
    new Thread(() -> pushHello()).start();
  }

  @OnWebSocketMessage
  public void onText(String message) {
    log.warn("jetty-serve received-message : {}", message);
  }

  @OnWebSocketClose
  public void onClose(int statusCode, String reason) {
    log.info("jetty-serve close reason {}", reason);
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