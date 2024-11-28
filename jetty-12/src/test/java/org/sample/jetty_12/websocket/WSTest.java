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

/**
 * @author gentjan kolicaj
 * @Date: 11/28/24 7:03 PM
 */
@Slf4j
public class WSTest {


  @ClientEndpoint
  @Getter
  public static class WSClientEndpoint {

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


}
