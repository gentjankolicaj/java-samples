package org.sample.websocket.annotated;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;


/**
 * @author gentjan kolicaj
 * @Date: 11/5/24 1:30 PM
 */
@Slf4j
@ServerEndpoint(EchoEndpointB.ENDPOINT_URI)
public class EchoEndpointB {

  /**
   * As opposed to servlets, WebSocket endpoints are instantiated multiple times. The container creates an instance of an endpoint per
   * connection to its deployment URI. Each instance is associated with one and only one connection. This facilitates keeping user state for
   * each connection and makes development easier, because there is only one thread executing the code of an endpoint instance at any given
   * time. This is why I store session references,
   */
  public static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
  public static final String ENDPOINT_URI = "/echoB";


  @OnOpen
  public void onOpen(Session session, EndpointConfig conf) {
    log.info("ws connection open.");
    SESSION_MAP.putIfAbsent(session.getId(), session);
  }


  @OnMessage
  public void onMessage(Session session, ByteBuffer buffer) {
    try {
      session.getBasicRemote().sendBinary(buffer);
    } catch (IOException e) {
      log.error("ws:onMessage()", e);
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("ws:onError", error);
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    log.info("ws: connection closed.");
  }
}