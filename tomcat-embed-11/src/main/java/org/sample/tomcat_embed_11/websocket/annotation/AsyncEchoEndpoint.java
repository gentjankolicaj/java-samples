package org.sample.tomcat_embed_11.websocket.annotation;

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
@ServerEndpoint(AsyncEchoEndpoint.WEBSOCKET_URI)
@Slf4j
@SuppressWarnings("unused")
public class AsyncEchoEndpoint {

  protected static final String WEBSOCKET_URI = "/annot_async_echo";

  @OnOpen
  public void onOpen(Session session) {
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
  public void onStringMessage(Session session, String message, boolean last) throws IOException {
    log.info("server session id {}, received string-message: {},last message: {}, sending-back: {}",
        session.getId(), message, last, message);

    //sent back to client same message
    session.getBasicRemote().sendText(message);

  }

  @OnMessage
  public void onByteMessage(Session session, ByteBuffer byteBuffer, boolean last)
      throws IOException {
    log.info("server session id {}, received byte-message: {},last message: {}, sending-back: {}",
        session.getId(), byteBuffer.array(), last, byteBuffer.array());

    //sent back to client same message
    session.getBasicRemote().sendBinary(byteBuffer);
  }


}
