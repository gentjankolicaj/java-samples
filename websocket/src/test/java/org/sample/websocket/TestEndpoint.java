package org.sample.websocket;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint("/ws/test")
@Getter
public class TestEndpoint {

  private Session session;

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    log.info("Session opened.");
  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    log.info("Received-message: {}", message);
    session.getBasicRemote().sendText("Return-back: " + message);
  }
}

