package org.sample.jetty_12.websocket;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.util.concurrent.LinkedBlockingDeque;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * @author gentjan kolicaj
 * @Date: 11/28/24 7:03 PM
 */
@Slf4j
public class WSTest {


  @ClientEndpoint
  @Getter
  public static class JakartaClientEndpoint {

    private final LinkedBlockingDeque<String> textQueue = new LinkedBlockingDeque<>();

    @OnOpen
    public void onOpen(Session session) {
      log.info("WebSocket Open: {}", session);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
      log.info("WebSocket Close: {}", closeReason);
    }


    @OnError
    public void onError(Throwable cause) {
      log.warn("WebSocket Error", cause);
    }

    @OnMessage
    public void onText(String message) {
      log.info("Text Message [{}]", message);
      textQueue.offer(message);
    }
  }

  @WebSocket
  @Getter
  public static class JettyClientEndpoint {

    private final LinkedBlockingDeque<String> textQueue = new LinkedBlockingDeque<>();

    @OnWebSocketOpen
    public void onOpen(org.eclipse.jetty.websocket.api.Session session) {
      log.info("Jetty-WebSocket Open: {}", session);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
      log.info("Jetty-WebSocket Close: {}", reason);
    }


    @OnWebSocketError
    public void onError(Throwable cause) {
      log.warn("Jetty-WebSocket Error", cause);
    }

    @OnWebSocketMessage
    public void onText(String message) {
      log.info("Jetty-Text Message [{}]", message);
      textQueue.offer(message);
    }
  }


}
