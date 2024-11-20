package org.sample.tomcat_embed_11.websocket.ssl.websocket;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gentjan kolicaj
 * @Date: 11/16/24 7:45 PM
 */
@ServerEndpoint(EchoEndpoint.WEBSOCKET_URI)
@Slf4j
@SuppressWarnings("unused")
public class EchoEndpoint {

  protected static final String WEBSOCKET_URI = "/ssl_basic_echo";

  private Session session;


  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    log.info("server session opened.");
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.warn("server session closed, id: {}, reason : {}", session.getId(), closeReason.toString());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.error("server session error, id: {}", session.getId(), throwable);
  }

  @OnMessage
  public void onStringMessage(Session session, String message) throws IOException {
    log.info("server session id {}, received string-message: {}, sending-back: {}", session.getId(),
        message, message);

    //sent back to client same message
    session.getBasicRemote().sendText(message);

  }

  @OnMessage
  public void onByteMessage(Session session, ByteBuffer byteBuffer) throws IOException {
    log.info("server session id {}, received byte-message: {}, sending-back: {}", session.getId(),
        byteBuffer.array(), byteBuffer.array());

    //sent back to client same message
    session.getBasicRemote().sendBinary(byteBuffer);
  }


}
